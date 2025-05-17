/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.services.EvaluationCriteriaService;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyenvanbao
 */
@RestController
@RequestMapping("/api/evaluation_criterias")
@CrossOrigin
public class ApiEvaluationCriteriaController {

    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;

    @Autowired
    private ModelMapper modelMapper;

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        evaluationCriteriaService.deleteEvaluationCriteria(id);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<EvaluationCriteriaDTO>> list(@RequestParam(required = false) Map<String, String> params) {
        List<EvaluationCriteriaDTO> ecd = modelMapper.map(
                evaluationCriteriaService.getEvaluationCriterias(params, true),
                new TypeToken<List<EvaluationCriteriaDTO>>() {
                }.getType());
        return new ResponseEntity<>(ecd, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public EvaluationCriteriaDTO update(@PathVariable int id, @RequestBody EvaluationCriteriaDTO evaluationCriteriaDTO) {

        if(id == evaluationCriteriaDTO.getId()){
            evaluationCriteriaService.addOrUpdateEvaluationCriteria(evaluationCriteriaDTO);
        }
        return evaluationCriteriaDTO;
    }

    @PostMapping("")
    public EvaluationCriteriaDTO create(@RequestBody EvaluationCriteriaDTO evaluationCriteriaDTO) {
        evaluationCriteriaService.addOrUpdateEvaluationCriteria(evaluationCriteriaDTO);
        return evaluationCriteriaDTO;
    }
}
