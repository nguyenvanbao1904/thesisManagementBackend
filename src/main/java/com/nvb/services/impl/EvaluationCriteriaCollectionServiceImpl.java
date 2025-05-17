/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.EvaluationCriteriaCollectionDetail;
import com.nvb.pojo.EvaluationCriteriaCollectionDetailPK;
import com.nvb.repositories.EvaluationCriteriaCollectionRepository;
import com.nvb.services.AcademicsStaffService;
import com.nvb.services.EvaluationCriteriaCollectionService;
import com.nvb.services.EvaluationCriteriaService;
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
public class EvaluationCriteriaCollectionServiceImpl implements EvaluationCriteriaCollectionService {

    @Autowired
    private EvaluationCriteriaCollectionRepository evaluationCriteriaCollectionRepository;

    @Autowired
    private AcademicsStaffService academicsStaffService;

    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;

    @Override
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params) {
        return this.getEvaluationCriteriaCollections(params, false);
    }

    @Override
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params, boolean pagination) {
        return evaluationCriteriaCollectionRepository.getEvaluationCriteriaCollections(params, pagination);
    }

    @Override
    public EvaluationCriteriaCollection addOrUpdateEvaluationCriteriaCollection(EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionDTO) {
        EvaluationCriteriaCollection evaluationCriteriaCollection;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff academicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffService.getAcademicStaff(Map.of("username", authentication.getName()));
        }

        if (evaluationCriteriaCollectionDTO.getId() == null) {
            evaluationCriteriaCollection = new EvaluationCriteriaCollection();
            evaluationCriteriaCollection.setName(evaluationCriteriaCollectionDTO.getName());
            evaluationCriteriaCollection.setDescription(evaluationCriteriaCollectionDTO.getDescription());
            if (academicStaff != null) {
                evaluationCriteriaCollection.setCreatedBy(academicStaff);
            }
            evaluationCriteriaCollection.setEvaluationCriteriaCollectionDetails(new HashSet<>());
            evaluationCriteriaCollection = evaluationCriteriaCollectionRepository.addOrUpdateEvaluationCriteriaCollection(evaluationCriteriaCollection);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("id", evaluationCriteriaCollectionDTO.getId().toString());
            List<EvaluationCriteriaCollection> existingCollections = evaluationCriteriaCollectionRepository.getEvaluationCriteriaCollectionsWithDetails(params, false);
            if (existingCollections.isEmpty()) {
                 throw new RuntimeException("EvaluationCriteriaCollection with ID " + evaluationCriteriaCollectionDTO.getId() + " not found for update.");
            }
            evaluationCriteriaCollection = existingCollections.get(0); 
            
            evaluationCriteriaCollection.setName(evaluationCriteriaCollectionDTO.getName());
            evaluationCriteriaCollection.setDescription(evaluationCriteriaCollectionDTO.getDescription());
            evaluationCriteriaCollection.setCreatedBy(academicStaff); 

            if (evaluationCriteriaCollection.getEvaluationCriteriaCollectionDetails() == null) {
                evaluationCriteriaCollection.setEvaluationCriteriaCollectionDetails(new HashSet<>());
            }
        }

        Set<EvaluationCriteriaCollectionDetail> currentDetails = evaluationCriteriaCollection.getEvaluationCriteriaCollectionDetails();
        Map<Integer, EvaluationCriteriaCollectionDetail> existingDetailsMap = new HashMap<>();
        for (EvaluationCriteriaCollectionDetail detail : currentDetails) {
            if (detail.getEvaluationCriteria() != null) { 
                 existingDetailsMap.put(detail.getEvaluationCriteria().getId(), detail);
            }
        }

        Set<Integer> dtoCriteriaIds = evaluationCriteriaCollectionDTO.getSelectedCriteriaIds() != null ? 
                                      new HashSet<>(evaluationCriteriaCollectionDTO.getSelectedCriteriaIds()) :
                                      new HashSet<>();

        currentDetails.removeIf(detail -> detail.getEvaluationCriteria() != null && 
                                        !dtoCriteriaIds.contains(detail.getEvaluationCriteria().getId()));

        if (evaluationCriteriaCollectionDTO.getEvaluationCriterias() != null) {
            for (EvaluationCriteriaDTO criteriaDTO : evaluationCriteriaCollectionDTO.getEvaluationCriterias()) {
                if (dtoCriteriaIds.contains(criteriaDTO.getId())) {
                    EvaluationCriteria evaluationCriteriaEntity = evaluationCriteriaService.getEvaluationCriteria(new HashMap<>(Map.of("id", criteriaDTO.getId().toString())));
                    if (evaluationCriteriaEntity == null) {
                        System.err.println("EvaluationCriteria entity not found for ID: " + criteriaDTO.getId());
                        continue; 
                    }

                    EvaluationCriteriaCollectionDetail detail = existingDetailsMap.get(criteriaDTO.getId());
                    if (detail == null) {
                        EvaluationCriteriaCollectionDetailPK pk = new EvaluationCriteriaCollectionDetailPK(evaluationCriteriaCollection.getId(), evaluationCriteriaEntity.getId());
                        detail = new EvaluationCriteriaCollectionDetail(pk);
                        detail.setEvaluationCriteriaCollection(evaluationCriteriaCollection); 
                        detail.setEvaluationCriteria(evaluationCriteriaEntity);
                        currentDetails.add(detail); 
                    }
                    detail.setWeight(criteriaDTO.getWeight() != null ? criteriaDTO.getWeight() : 0f);
                }
            }
        }

        return evaluationCriteriaCollectionRepository.addOrUpdateEvaluationCriteriaCollection(evaluationCriteriaCollection);
    }

    @Override
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollectionsWithDetails(Map<String, String> params, boolean pagination) {
        return evaluationCriteriaCollectionRepository.getEvaluationCriteriaCollectionsWithDetails(params, pagination);
    }

    @Override
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollectionsWithDetails(Map<String, String> params) {
        return this.getEvaluationCriteriaCollectionsWithDetails(params, false);
    }

    @Override
    public void deleteEvaluationCriteriaCollection(int id) {
        evaluationCriteriaCollectionRepository.deleteEvaluationCriteriaCollection(id);
    }

}
