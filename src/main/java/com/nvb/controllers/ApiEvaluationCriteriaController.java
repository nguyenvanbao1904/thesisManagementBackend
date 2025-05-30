/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.services.EvaluationCriteriaService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
    @Qualifier("evaluationCriteriaWebAppValidator")
    private WebAppValidator evaluationCriteriaWebAppValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaWebAppValidator);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        evaluationCriteriaService.delete(id);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<EvaluationCriteriaDTO>> list(@RequestParam(required = false) Map<String, String> params) {
        List<EvaluationCriteriaDTO> evaluationCriterias = evaluationCriteriaService.getAll(params, true);
        return new ResponseEntity<>(evaluationCriterias, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationCriteriaDTO> update(@PathVariable int id, @RequestBody @Valid EvaluationCriteriaDTO evaluationCriteriaDTO,
            BindingResult bindingResult
    ) {
        if (id != evaluationCriteriaDTO.getId() || bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EvaluationCriteriaDTO updatedDto = evaluationCriteriaService.addOrUpdate(evaluationCriteriaDTO);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<EvaluationCriteriaDTO> create(@RequestBody @Valid EvaluationCriteriaDTO evaluationCriteriaDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EvaluationCriteriaDTO createdDto = evaluationCriteriaService.addOrUpdate(evaluationCriteriaDTO);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }
}
