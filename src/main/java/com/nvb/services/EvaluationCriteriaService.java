/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationCriteriaService {

    List<EvaluationCriteria> getEvaluationCriterias(Map<String, String> params);

    List<EvaluationCriteria> getEvaluationCriterias(Map<String, String> params, boolean pagination);

    EvaluationCriteria getEvaluationCriteria(Map<String, String> params);
    
    EvaluationCriteria addOrUpdateEvaluationCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO);
    
    void deleteEvaluationCriteria(int id);
}
