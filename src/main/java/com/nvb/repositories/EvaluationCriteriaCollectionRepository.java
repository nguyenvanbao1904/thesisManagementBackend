/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.EvaluationCriteriaCollection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationCriteriaCollectionRepository {

    List<EvaluationCriteriaCollection> getAll(Map<String, String> params, boolean pagination, boolean details);

    EvaluationCriteriaCollection addOrUpdate(EvaluationCriteriaCollection evaluationCriteriaCollection);

    void delete(int id);
    
    EvaluationCriteriaCollection get(Map<String, String> params);
}
