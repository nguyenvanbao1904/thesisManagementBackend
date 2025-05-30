/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.services.EvaluationCriteriaCollectionService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.HashMap;
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
@RequestMapping("/api/evaluation_criteria_collections")
@CrossOrigin
public class ApiEvaluationCriteriaCollectionController {

    @Autowired
    private EvaluationCriteriaCollectionService evaluationCriteriaCollectionService;
    
    @Autowired
    @Qualifier("evaluationCriteriaCollectionWebAppValidator")
    private WebAppValidator evaluationCriteriaCollectionWebAppValidator;

    @InitBinder()
    public void initEvaluationCriteriaCollectionBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaCollectionWebAppValidator);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        evaluationCriteriaCollectionService.delete(id);
    }

    @GetMapping("")
    public ResponseEntity<List<EvaluationCriteriaCollectionDTO>> list(@RequestParam Map<String, String> params) {
        List<EvaluationCriteriaCollectionDTO> collections = evaluationCriteriaCollectionService.getAll(params, true);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationCriteriaCollectionDTO> update(
            @PathVariable int id,
            @RequestBody @Valid EvaluationCriteriaCollectionDTO dto,
            BindingResult bindingResult) {
        if (id != dto.getId() || bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EvaluationCriteriaCollectionDTO updatedDto = evaluationCriteriaCollectionService.addOrUpdate(dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<EvaluationCriteriaCollectionDTO> create(
            @RequestBody @Valid EvaluationCriteriaCollectionDTO dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EvaluationCriteriaCollectionDTO createdDto = evaluationCriteriaCollectionService.addOrUpdate(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationCriteriaCollectionDTO> retrieve(@PathVariable(value = "id") int id) {
        EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionDTO = evaluationCriteriaCollectionService.get(new HashMap<>(Map.of("id", String.valueOf(id))));
        return new ResponseEntity<>(evaluationCriteriaCollectionDTO, HttpStatus.OK);
    }
}
