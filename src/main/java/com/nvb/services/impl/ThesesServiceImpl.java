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
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
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
    private LocalSessionFactoryBean factory;

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
        Session session = factory.getObject().getCurrentSession();

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffService.getAcademicStaff(Map.of("username", authentication.getName()));
        }

        if (thesesDTO.getId() == null) {
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
            thesis.setCommitteeId(null); // bo sung sau

            // Thiết lập các mối quan hệ khác
            if (thesesDTO.getEvaluationCriteriaCollectionId() != null) {
                Map<String, String> params = new HashMap<>();
                params.put("id", thesesDTO.getEvaluationCriteriaCollectionId().getId().toString());
                thesis.setEvaluationCriteriaCollectionId(
                        evaluationCriteriaCollectionService.getEvaluationCriteriaCollection(params)
                );
            }

            if (thesesDTO.getReviewerId() != null) {
                Lecturer reviewer = session.get(Lecturer.class, thesesDTO.getReviewerId().getId());
                thesis.setReviewerId(reviewer);
            }

            // Đối với giảng viên hướng dẫn, do mối quan hệ được quản lý từ phía Lecturer
            // nên cần lấy lecturer từ session và cập nhật thesesSupervisors
            if (thesesDTO.getLecturers() != null && !thesesDTO.getLecturers().isEmpty()) {
                Set<Lecturer> lecturers = new HashSet<>();
                for (Lecturer lecturer : thesesDTO.getLecturers()) {
                    Lecturer managedLecturer = session.get(Lecturer.class, lecturer.getId());
                    if (managedLecturer != null) {
                        lecturers.add(managedLecturer);

                        // Thêm thesis vào collection của lecturer
                        managedLecturer.getThesesSupervisors().add(thesis);
                    }
                }

                // Cập nhật thesis
                thesis.setLecturers(lecturers);
            }

            // Tương tự với sinh viên
            if (thesesDTO.getStudents() != null && !thesesDTO.getStudents().isEmpty()) {
                Set<Student> students = new HashSet<>();
                for (Student student : thesesDTO.getStudents()) {
                    Student managedStudent = session.get(Student.class, student.getId());
                    if (managedStudent != null) {
                        students.add(managedStudent);
                        // Thêm thesis vào collection của student
                        managedStudent.gettheses().add(thesis);
                    }
                }

                // Cập nhật thesis
                thesis.setStudents(students);
            }

            // Lưu thesis
            thesis = thesesRepository.addOrUpdate(thesis);
        }

        return thesis;
    }
}
