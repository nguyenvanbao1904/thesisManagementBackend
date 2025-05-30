/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaCollectionListDTO;
// import com.nvb.pojo.EvaluationCriteriaCollection; // Sẽ bị xóa
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationCriteriaCollectionService {

    List<EvaluationCriteriaCollectionDTO> getAll(Map<String, String> params);

    List<EvaluationCriteriaCollectionDTO> getAll(Map<String, String> params, boolean pagination);

    List<EvaluationCriteriaCollectionDTO> getAll(Map<String, String> params, boolean pagination, boolean details);

    EvaluationCriteriaCollectionDTO get(Map<String, String> params);

    EvaluationCriteriaCollectionDTO addOrUpdate(EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionDTO);

    void delete(int id);

    List<EvaluationCriteriaCollectionListDTO> getAllForListView(Map<String, String> params, boolean pagination);

}
