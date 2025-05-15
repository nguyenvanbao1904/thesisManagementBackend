/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.Thesis;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface ThesesRepository {
    List<Thesis> getTheses(Map<String, String> params);
    List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params);
    List<EvaluationCriteriaDTO> getEvaluationCriteriasDTO(Map<String, String> params);
    List<EvaluationCriteria> getEvaluationCriterias(Map<String, String> params);
    EvaluationCriteria addEvaluationCriteria(EvaluationCriteria evaluationCriteria);
    EvaluationCriteriaCollection addOrUpdateEvaluationCriteriaCollection(EvaluationCriteriaCollection evaluationCriteriaCollection);
    EvaluationCriteria findEvaluationCriteriaEntityById(Integer id);
}
