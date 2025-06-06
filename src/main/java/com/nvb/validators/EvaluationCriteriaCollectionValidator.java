/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.services.EvaluationCriteriaCollectionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author nguyenvanbao
 */
@Component
public class EvaluationCriteriaCollectionValidator implements Validator {

    @Autowired
    private EvaluationCriteriaCollectionService evaluationCriteriaCollectionService; 

    @Override
    public boolean supports(Class<?> clazz) {
        return EvaluationCriteriaCollectionDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EvaluationCriteriaCollectionDTO collectionDto = (EvaluationCriteriaCollectionDTO) target;

        // Kiểm tra trùng lặp name
        if (collectionDto.getName() != null && !collectionDto.getName().isBlank()) {
            EvaluationCriteriaCollectionDTO existing = evaluationCriteriaCollectionService.get(new HashMap<>(Map.of("name", collectionDto.getName().trim())));
            if (collectionDto.getId() == null) {
                if (existing != null) {
                    errors.rejectValue("name", "evaluationCriteriaCollection.name.duplicateMsg");
                }
            } else {
                if (existing != null && existing.getId() != null && !existing.getId().equals(collectionDto.getId())) {
                    errors.rejectValue("name", "evaluationCriteriaCollection.name.duplicateMsg");
                }
            }
        }

        List<EvaluationCriteriaDTO> criteriaList = collectionDto.getEvaluationCriterias();
        if (criteriaList != null && !criteriaList.isEmpty()) {
            float totalWeight = 0;
            for (int i = 0; i < criteriaList.size(); i++) {
                EvaluationCriteriaDTO criteriaDto = criteriaList.get(i);
                if (criteriaDto.getWeight() != null) {
                    totalWeight += criteriaDto.getWeight();
                }
            }
            if (Math.abs(totalWeight - 1.0f) > 0.00001f) {
                errors.reject("evaluationCriteriaCollection.totalWeight.invalidMsg", "Tổng trọng số của các tiêu chí phải bằng 1 (100%).");
            }
        }
    }
}
