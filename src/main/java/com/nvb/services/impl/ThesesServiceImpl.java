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
import java.util.List;
import java.util.Map;
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
        Thesis thesis;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff academicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffService.getAcademicStaff(Map.of("username", authentication.getName()));
        }

        if (thesesDTO.getId() == null) {
            thesis = new Thesis();
            thesis.setStatus(ThesisStatus.DRAFT.toString());
        } else {
            thesis = thesesRepository.getThesis(Map.of("id", thesesDTO.getId().toString()));
            thesis.getLecturers().forEach(l -> l.getThesesSupervisors().remove(thesis));
            thesis.getStudents().forEach(s -> s.getTheses().remove(thesis));
        }
        thesis.setTitle(thesesDTO.getTitle());
        thesis.setDescription(thesesDTO.getDescription());
        thesis.setCreatedBy(academicStaff);
        thesis.setCommitteeId(null);
        int ecId = thesesDTO.getEvaluationCriteriaCollectionId().getId();
        thesis.setEvaluationCriteriaCollectionId(
                evaluationCriteriaCollectionService.getEvaluationCriteriaCollection(Map.of("id", String.valueOf(ecId)))
        );
        thesis.setReviewerId(thesesDTO.getReviewerId());
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
        thesis.setLecturers(thesesDTO.getLecturers());
        thesis.setStudents(thesesDTO.getStudents());
        return thesesRepository.addOrUpdate(thesis);
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
