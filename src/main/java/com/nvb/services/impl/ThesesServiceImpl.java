/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvb.dto.ThesesDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Student;
import com.nvb.pojo.Thesis;
import com.nvb.pojo.ThesisStatus;
import com.nvb.repositories.ThesesRepository;
import com.nvb.services.AcademicsStaffService;
import com.nvb.services.EvaluationCriteriaCollectionService;
import com.nvb.services.ThesesService;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Transactional
@Service
public class ThesesServiceImpl implements ThesesService {

    @Autowired
    private ThesesRepository thesesRepository;

    @Autowired
    private AcademicsStaffService academicsStaffService;

    @Autowired
    private EvaluationCriteriaCollectionService evaluationCriteriaCollectionService;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Thesis> getTheses(Map<String, String> params) {
        return this.getTheses(params, false);
    }

    @Override
    public List<Thesis> getTheses(Map<String, String> params, boolean pagination) {
        return thesesRepository.getTheses(params, pagination);
    }

    @Override
    public Thesis addOrUpdate(ThesesDTO thesesDTO) {
        Thesis thesis = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff academicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffService.getAcademicStaff(Map.of("username", authentication.getName()));
        }

        if (thesesDTO.getId() == null) {
            // Create new thesis
            thesis = new Thesis();
            thesis.setTitle(thesesDTO.getTitle());
            thesis.setDescription(thesesDTO.getDescription());
            thesis.setStatus(ThesisStatus.DRAFT.toString());

            try {
                String originalFilename = thesesDTO.getFile().getOriginalFilename();
                String publicId = originalFilename != null
                        ? originalFilename.replaceAll("\\s+", "_")
                        : "uploaded_file";
                Map res = cloudinary.uploader().upload(thesesDTO.getFile().getBytes(), ObjectUtils.asMap("resource_type", "raw", "public_id", publicId));
                thesis.setFileUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

            thesis.setCreatedBy(academicStaff);
            thesis.setCommitteeId(null);

            Map<String, String> params = new HashMap<>();
            params.put("id", thesesDTO.getEvaluationCriteriaCollectionId().getId().toString());
            thesis.setEvaluationCriteriaCollectionId(
                    evaluationCriteriaCollectionService.getEvaluationCriteriaCollection(params)
            );

            thesis.setReviewerId(thesesDTO.getReviewerId());
            thesis.setLecturers(thesesDTO.getLecturers());
            thesis.setStudents(thesesDTO.getStudents());

            thesis = thesesRepository.addOrUpdate(thesis);
        } else {
            // Update existing thesis
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(thesesDTO.getId()));
            thesis = thesesRepository.getThesis(params);
            
            thesis.setTitle(thesesDTO.getTitle());
            thesis.setDescription(thesesDTO.getDescription());
            
            if (thesesDTO.getFile() != null && !thesesDTO.getFile().getOriginalFilename().isBlank()) {
                try {
                    String originalFilename = thesesDTO.getFile().getOriginalFilename();
                    String publicId = originalFilename != null
                            ? originalFilename.replaceAll("\\s+", "_")
                            : "uploaded_file";
                    Map res = cloudinary.uploader().upload(thesesDTO.getFile().getBytes(), ObjectUtils.asMap("resource_type", "raw", "public_id", publicId));
                    thesis.setFileUrl(res.get("secure_url").toString());
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            
            thesis.setCreatedBy(academicStaff);
            thesis.setCommitteeId(null);

            params.clear();
            params.put("id", thesesDTO.getEvaluationCriteriaCollectionId().getId().toString());
            thesis.setEvaluationCriteriaCollectionId(
                    evaluationCriteriaCollectionService.getEvaluationCriteriaCollection(params)
            );

            thesis.setReviewerId(thesesDTO.getReviewerId());
            
            // Handle lecturers relationship update
            Set<Lecturer> currentLecturers = thesis.getLecturers();
            Set<Lecturer> newLecturers = thesesDTO.getLecturers();
            
            // Create maps of current lecturers by ID for efficient lookup
            Map<Integer, Lecturer> currentLecturersMap = new HashMap<>();
            for (Lecturer lecturer : currentLecturers) {
                currentLecturersMap.put(lecturer.getId(), lecturer);
            }
            
            // Remove lecturers that are no longer associated
            for (Lecturer lecturer : new HashSet<>(currentLecturers)) {
                boolean stillAssociated = false;
                for (Lecturer newLecturer : newLecturers) {
                    if (newLecturer.getId().equals(lecturer.getId())) {
                        stillAssociated = true;
                        break;
                    }
                }
                
                if (!stillAssociated) {
                    // Get managed lecturer to ensure changes are persisted
                    Lecturer managedLecturer = thesesRepository.getLecturerWithTheses(lecturer.getId());
                    if (managedLecturer != null) {
                        // Remove thesis from lecturer's collection
                        managedLecturer.getThesesSupervisors().remove(thesis);
                        // Remove lecturer from thesis's collection
                        currentLecturers.remove(lecturer);
                    }
                }
            }
            
            // Add new lecturers that aren't already associated
            for (Lecturer newLecturer : newLecturers) {
                boolean alreadyAssociated = false;
                for (Lecturer currentLecturer : currentLecturers) {
                    if (currentLecturer.getId().equals(newLecturer.getId())) {
                        alreadyAssociated = true;
                        break;
                    }
                }
                
                if (!alreadyAssociated) {
                    // Get managed lecturer to ensure changes are persisted
                    Lecturer managedLecturer = thesesRepository.getLecturerWithTheses(newLecturer.getId());
                    if (managedLecturer != null) {
                        // Add thesis to lecturer's collection
                        managedLecturer.getThesesSupervisors().add(thesis);
                        // Add lecturer to thesis's collection
                        currentLecturers.add(managedLecturer);
                    }
                }
            }
            
            // Handle students relationship update
            Set<Student> currentStudents = thesis.getStudents();
            Set<Student> newStudents = thesesDTO.getStudents();
            
            // Create maps of current students by ID for efficient lookup
            Map<Integer, Student> currentStudentsMap = new HashMap<>();
            for (Student student : currentStudents) {
                currentStudentsMap.put(student.getId(), student);
            }
            
            // Remove students that are no longer associated
            for (Student student : new HashSet<>(currentStudents)) {
                boolean stillAssociated = false;
                for (Student newStudent : newStudents) {
                    if (newStudent.getId().equals(student.getId())) {
                        stillAssociated = true;
                        break;
                    }
                }
                
                if (!stillAssociated) {
                    // Get managed student to ensure changes are persisted
                    Student managedStudent = thesesRepository.getStudentWithTheses(student.getId());
                    if (managedStudent != null) {
                        // Remove thesis from student's collection
                        managedStudent.gettheses().remove(thesis);
                        // Remove student from thesis's collection
                        currentStudents.remove(student);
                    }
                }
            }
            
            // Add new students that aren't already associated
            for (Student newStudent : newStudents) {
                boolean alreadyAssociated = false;
                for (Student currentStudent : currentStudents) {
                    if (currentStudent.getId().equals(newStudent.getId())) {
                        alreadyAssociated = true;
                        break;
                    }
                }
                
                if (!alreadyAssociated) {
                    // Get managed student to ensure changes are persisted
                    Student managedStudent = thesesRepository.getStudentWithTheses(newStudent.getId());
                    if (managedStudent != null) {
                        // Add thesis to student's collection
                        managedStudent.gettheses().add(thesis);
                        // Add student to thesis's collection
                        currentStudents.add(managedStudent);
                    }
                }
            }
            
            thesis = thesesRepository.addOrUpdate(thesis);
        }

        return thesis;
    }

    @Override
    public Thesis getThesis(Map<String, String> params) {
        return thesesRepository.getThesis(params);
    }

    @Override
    public void deleteThesis(int id) {
        thesesRepository.deleteThesis(id);
    }
}
