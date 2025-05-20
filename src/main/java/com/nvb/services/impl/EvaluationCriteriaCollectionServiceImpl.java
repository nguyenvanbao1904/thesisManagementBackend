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
            evaluationCriteriaCollection.setEvaluationCriteriaCollectionDetails(new HashSet<>());
        } else {
            evaluationCriteriaCollection = evaluationCriteriaCollectionRepository.getEvaluationCriteriaCollection(
                    Map.of("id", evaluationCriteriaCollectionDTO.getId().toString()));
            
            if (evaluationCriteriaCollection == null) {
                throw new RuntimeException("EvaluationCriteriaCollection with ID " + 
                        evaluationCriteriaCollectionDTO.getId() + " not found for update.");
            }
            
            evaluationCriteriaCollection.getEvaluationCriteriaCollectionDetails().clear();
        }

        evaluationCriteriaCollection.setName(evaluationCriteriaCollectionDTO.getName());
        evaluationCriteriaCollection.setDescription(evaluationCriteriaCollectionDTO.getDescription());
        evaluationCriteriaCollection.setCreatedBy(academicStaff);

        // luu de lay id
        evaluationCriteriaCollection = evaluationCriteriaCollectionRepository.addOrUpdateEvaluationCriteriaCollection(evaluationCriteriaCollection);
        
        if (evaluationCriteriaCollectionDTO.getSelectedCriterias() != null && 
            evaluationCriteriaCollectionDTO.getEvaluationCriterias() != null) {
            
            Map<Integer, Float> criteriaWeights = new HashMap<>();
            for (EvaluationCriteriaDTO criteriaDTO : evaluationCriteriaCollectionDTO.getEvaluationCriterias()) {
                if (criteriaDTO.getId() != null && criteriaDTO.getWeight() != null) {
                    criteriaWeights.put(criteriaDTO.getId(), criteriaDTO.getWeight());
                }
            }
            
            for (EvaluationCriteria criteria : evaluationCriteriaCollectionDTO.getSelectedCriterias()) {
                EvaluationCriteriaCollectionDetailPK pk = new EvaluationCriteriaCollectionDetailPK(
                        evaluationCriteriaCollection.getId(), criteria.getId());
                
                EvaluationCriteriaCollectionDetail detail = new EvaluationCriteriaCollectionDetail(pk);
                detail.setEvaluationCriteriaCollection(evaluationCriteriaCollection);
                detail.setEvaluationCriteria(criteria);
                
                Float weight = criteriaWeights.getOrDefault(criteria.getId(), 0f);
                detail.setWeight(weight);
                
                evaluationCriteriaCollection.getEvaluationCriteriaCollectionDetails().add(detail);
            }
            
            evaluationCriteriaCollection = evaluationCriteriaCollectionRepository.addOrUpdateEvaluationCriteriaCollection(evaluationCriteriaCollection);
        }

        return evaluationCriteriaCollection;
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

    @Override
    public EvaluationCriteriaCollection getEvaluationCriteriaCollection(Map<String, String> params) {
        return evaluationCriteriaCollectionRepository.getEvaluationCriteriaCollection(params);
    }

}
