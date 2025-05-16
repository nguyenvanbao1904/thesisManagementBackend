/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.EvaluationCriteria;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationCriteriaRepository {

    List<EvaluationCriteria> getEvaluationCriterias(Map<String, String> params, boolean pagination);

    EvaluationCriteria getEvaluationCriteria(Map<String, String> params);

    EvaluationCriteria addOrUpdateEvaluationCriteria(EvaluationCriteria evaluationCriteria);

    void deleteEvaluationCriteria(int id);

}
