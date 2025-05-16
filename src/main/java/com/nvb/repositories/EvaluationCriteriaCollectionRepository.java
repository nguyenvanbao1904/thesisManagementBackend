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

    List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params, boolean pagination);

    EvaluationCriteriaCollection addOrUpdateEvaluationCriteriaCollection(EvaluationCriteriaCollection EvaluationCriteriaCollection);

    List<EvaluationCriteriaCollection> getEvaluationCriteriaCollectionsWithDetails(Map<String, String> params, boolean pagination);

    void deleteEvaluationCriteriaCollection(int id);
}
