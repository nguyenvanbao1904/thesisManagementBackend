/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvb.dto.ThesesDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Thesis;
import com.nvb.pojo.ThesisStatus;
import com.nvb.repositories.ThesesRepository;
import com.nvb.services.ThesesService;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Student;
import org.modelmapper.ModelMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvb.repositories.LecturerRepository;
import com.nvb.pojo.User;
import com.nvb.repositories.UserRepository;
import com.nvb.repositories.EvaluationCriteriaCollectionRepository;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.repositories.StudentRepository;
import java.util.HashSet;
import java.util.Set;
import com.nvb.dto.ThesesListDTO;
import com.nvb.pojo.Committee;
import com.nvb.repositories.CommitteeRepository;

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
    private UserRepository userRepository;

    @Autowired
    private EvaluationCriteriaCollectionRepository evaluationCriteriaCollectionRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    public List<ThesesDTO> getAll(Map<String, String> params) {
        return getAll(params, false, false);
    }

    @Override
    public List<ThesesDTO> getAll(Map<String, String> params, boolean pagination) {
        return getAll(params, pagination, false);
    }

    @Override
    public List<ThesesDTO> getAll(Map<String, String> params, boolean pagination, boolean details) {
        return thesesRepository.getAll(params, pagination, details)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ThesesDTO addOrUpdate(ThesesDTO thesesDTO) {
        Thesis thesis;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff createdByAcademicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userRepository.get(Map.of("username", authentication.getName()));
            if (currentUser != null && currentUser instanceof AcademicStaff) {
                createdByAcademicStaff = (AcademicStaff) currentUser;
            }
        }

        if (thesesDTO.getId() == null) {
            thesis = new Thesis();
            thesis.setStatus(ThesisStatus.DRAFT.toString());
        } else {
            thesis = thesesRepository.get(Map.of("id", thesesDTO.getId().toString()));
            if (thesis.getLecturers() != null) {
                thesis.getLecturers().forEach(l -> l.getThesesSupervisors().remove(thesis));
            }
            if (thesis.getStudents() != null) {
                thesis.getStudents().forEach(s -> s.getTheses().remove(thesis));
            }
        }
        modelMapper.map(thesesDTO, thesis);
        thesis.setCreatedBy(createdByAcademicStaff);

        // Set EvaluationCriteriaCollection
        if (thesesDTO.getEvaluationCriteriaCollectionId() != null) {
            EvaluationCriteriaCollection collectionEntity = evaluationCriteriaCollectionRepository.get(
                    Map.of("id", String.valueOf(thesesDTO.getEvaluationCriteriaCollectionId()))
            );
            thesis.setEvaluationCriteriaCollectionId(collectionEntity);
        } else {
            thesis.setEvaluationCriteriaCollectionId(null);
        }

        // Set Reviewer
        if (thesesDTO.getReviewerId() != null) {
            Lecturer reviewer = lecturerRepository.get(Map.of("id", String.valueOf(thesesDTO.getReviewerId())));
            thesis.setReviewerId(reviewer);
        } else {
            thesis.setReviewerId(null);
        }
        
        // Set Committee
        if (thesesDTO.getCommitteeId()!= null) {
            Committee committee = committeeRepository.get(Map.of("id", String.valueOf(thesesDTO.getCommitteeId())));
            thesis.setCommitteeId(committee);
        } else {
            thesis.setCommitteeId(null);
        }

        // Set Lecturers
        if (thesesDTO.getLecturerIds() != null) {
            java.util.Set<Lecturer> lecturers = thesesDTO.getLecturerIds().stream()
                    .map(id -> lecturerRepository.get(Map.of("id", String.valueOf(id))))
                    .filter(java.util.Objects::nonNull)
                    .collect(java.util.stream.Collectors.toSet());
            thesis.setLecturers(lecturers);
        } else {
            thesis.setLecturers(null);
        }

        // Set Students
        if (thesesDTO.getStudentIds() != null && !thesesDTO.getStudentIds().isEmpty()) {
            Set<Student> students = thesesDTO.getStudentIds().stream()
                    .map(id -> studentRepository.get(Map.of("id", String.valueOf(id)), true))
                    .filter(java.util.Objects::nonNull)
                    .collect(java.util.stream.Collectors.toSet());
            thesis.setStudents(students);
        } else {
            thesis.setStudents(new HashSet<>());
        }

        // File upload giữ nguyên như cũ...
        if (thesesDTO.getFile() != null && !thesesDTO.getFile().getOriginalFilename().isBlank()) {
            try {
                String originalFilename = thesesDTO.getFile().getOriginalFilename();
                String publicId = originalFilename != null
                        ? originalFilename.replaceAll("\\s+", "_")
                        : "uploaded_file";

                Map uploadResult = cloudinary.uploader().upload(
                        thesesDTO.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "raw", "public_id", publicId)
                );
                thesis.setFileUrl(uploadResult.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("File upload error: " + ex.getMessage());
            }
        }

        return toDTO(thesesRepository.addOrUpdate(thesis));
    }

    @Override
    public ThesesDTO get(Map<String, String> params) {
        Thesis thesis = thesesRepository.get(params);
        return thesis != null ? toDTO(thesis) : null;
    }

    @Override
    public void delete(int id) {
        thesesRepository.delete(id);
    }

    @Override
    public boolean isStudentInAnotherActiveThesis(Integer studentUserId, Integer currentThesisIdToExclude) {
        if (studentUserId == null) {
            return false;
        }
        long count = this.thesesRepository.countThesesByStudentAndNotThisThesis(studentUserId, currentThesisIdToExclude);
        return count > 0;
    }

    @Override
    public List<ThesesListDTO> getAllForListView(Map<String, String> params, boolean pagination) {
        return thesesRepository.getAll(params, pagination, false)
                .stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    private ThesesListDTO toListDTO(Thesis thesis) {
        return new ThesesListDTO(thesis.getId(), thesis.getTitle(), thesis.getDescription(), thesis.getStatus(), thesis.getFileUrl());
    }

    private ThesesDTO toDTO(Thesis thesis) {
        // Tạo DTO mới và map các trường cơ bản
        ThesesDTO dto = new ThesesDTO();
        dto.setId(thesis.getId());
        dto.setTitle(thesis.getTitle());
        dto.setDescription(thesis.getDescription());
        dto.setStatus(thesis.getStatus());
        dto.setFileUrl(thesis.getFileUrl());

        // Map committeeId
        if (thesis.getCommitteeId() != null) {
            dto.setCommitteeId(thesis.getCommitteeId().getId());
            dto.setCommitteeName("Hội đồng ID: " + thesis.getCommitteeId().getId()
                    + (thesis.getCommitteeId().getLocation() != null ? " - " + thesis.getCommitteeId().getLocation() : ""));
        }

        // Map evaluationCriteriaCollection
        if (thesis.getEvaluationCriteriaCollectionId() != null) {
            dto.setEvaluationCriteriaCollectionId(thesis.getEvaluationCriteriaCollectionId().getId());
            dto.setEvaluationCriteriaCollectionName(thesis.getEvaluationCriteriaCollectionId().getName());
        }

        // Map reviewer
        if (thesis.getReviewerId() != null) {
            dto.setReviewerId(thesis.getReviewerId().getId());
            dto.setReviewerName((thesis.getReviewerId().getAcademicDegree() != null
                    ? thesis.getReviewerId().getAcademicDegree() + ". " : "")
                    + thesis.getReviewerId().getFirstName() + " "
                    + thesis.getReviewerId().getLastName());
        }

        // Map lecturers
        if (thesis.getLecturers() != null) {
            dto.setLecturerIds(thesis.getLecturers().stream()
                    .map(Lecturer::getId)
                    .collect(Collectors.toSet()));
        }

        // Map students
        if (thesis.getStudents() != null) {
            dto.setStudentIds(thesis.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet()));

            List<String> studentFullNames = thesis.getStudents().stream()
                    .map(s -> s.getFirstName() + " " + s.getLastName()
                    + (s.getStudentId() != null ? " (" + s.getStudentId() + ")" : ""))
                    .collect(Collectors.toList());
        }
        return dto;
    }

    @Override
    public List<ThesesDTO> getByIds(List<Integer> ids) {
        return thesesRepository.getByIds(ids)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}
