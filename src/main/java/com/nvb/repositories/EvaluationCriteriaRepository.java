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

    List<EvaluationCriteria> getAll(Map<String, String> params, boolean pagination);

    EvaluationCriteria get(Map<String, String> params);

    EvaluationCriteria addOrUpdate(EvaluationCriteria evaluationCriteria);

    void delete(int id);
    
    List<EvaluationCriteria> getByIds(List<Integer> ids);
}
