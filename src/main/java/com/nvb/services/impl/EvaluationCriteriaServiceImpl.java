/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.repositories.EvaluationCriteriaRepository;
import com.nvb.services.EvaluationCriteriaService;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Transactional
@Service
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService{
    
    @Autowired
    private EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<EvaluationCriteriaDTO> getAll(Map<String, String> params) {
        return getAll(params, false);
    }

    @Override
    public List<EvaluationCriteriaDTO> getAll(Map<String, String> params, boolean pagination) {
        return evaluationCriteriaRepository.getAll(params, pagination)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public EvaluationCriteriaDTO get(Map<String, String> params) {
        EvaluationCriteria entity = evaluationCriteriaRepository.get(params);
        return entity != null ? toDTO(entity) : null;
    }

    @Override
    public EvaluationCriteriaDTO addOrUpdate(EvaluationCriteriaDTO evaluationCriteriaDTO) {
        EvaluationCriteria entity;
        if (evaluationCriteriaDTO.getId() == null) {
            entity = new EvaluationCriteria();
        } else {
            entity = evaluationCriteriaRepository.get(Map.of("id", evaluationCriteriaDTO.getId().toString()));
        }
        modelMapper.map(evaluationCriteriaDTO, entity);
        return toDTO(evaluationCriteriaRepository.addOrUpdate(entity));
    }

    @Override
    public void delete(int id) {
        evaluationCriteriaRepository.delete(id);
    }
    
    private EvaluationCriteriaDTO toDTO(EvaluationCriteria entity) {
        return modelMapper.map(entity, EvaluationCriteriaDTO.class);
    }
}
