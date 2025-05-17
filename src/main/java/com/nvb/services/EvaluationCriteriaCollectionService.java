/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.pojo.EvaluationCriteriaCollection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationCriteriaCollectionService {

    List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params);

    List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params, boolean pagination);
    
    EvaluationCriteriaCollection getEvaluationCriteriaCollection(Map<String, String> params);

    EvaluationCriteriaCollection addOrUpdateEvaluationCriteriaCollection(EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionDTO);

    List<EvaluationCriteriaCollection> getEvaluationCriteriaCollectionsWithDetails(Map<String, String> params, boolean pagination);

    List<EvaluationCriteriaCollection> getEvaluationCriteriaCollectionsWithDetails(Map<String, String> params);

    void deleteEvaluationCriteriaCollection(int id);

}
