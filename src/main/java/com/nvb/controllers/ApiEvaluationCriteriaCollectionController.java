/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.services.EvaluationCriteriaCollectionService;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/api/evaluation_criteria_collections")
@CrossOrigin
public class ApiEvaluationCriteriaCollectionController {

    @Autowired
    private EvaluationCriteriaCollectionService evaluationCriteriaCollectionService;

    @Autowired
    private ModelMapper modelMapper;

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        evaluationCriteriaCollectionService.deleteEvaluationCriteriaCollection(id);
    }

    @GetMapping("")
    public ResponseEntity<List<EvaluationCriteriaCollectionDTO>> list(@RequestParam Map<String, String> params) {
        List<EvaluationCriteriaCollection> lsEcc = evaluationCriteriaCollectionService.getEvaluationCriteriaCollectionsWithDetails(params, true);
        List<EvaluationCriteriaCollectionDTO> rs = lsEcc.stream()
                .map(ecc -> {
                    EvaluationCriteriaCollectionDTO eccd = new EvaluationCriteriaCollectionDTO();
                    eccd.setId(ecc.getId());
                    eccd.setDescription(ecc.getDescription());
                    eccd.setName(ecc.getName());
                    eccd.setCreatedBy(ecc.getCreatedBy());

                    eccd.setEvaluationCriterias(
                            ecc.getEvaluationCriteriaCollectionDetails().stream()
                                    .map(detail -> {
                                        EvaluationCriteriaDTO dto = modelMapper.map(detail.getEvaluationCriteria(), EvaluationCriteriaDTO.class);
                                        dto.setWeight(detail.getWeight());
                                        return dto;
                                    })
                                    .toList()
                    );

                    return eccd;
                })
                .toList();

        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public EvaluationCriteriaCollectionDTO update(
            @PathVariable int id,
            @RequestBody EvaluationCriteriaCollectionDTO dto) {

        if (id == dto.getId()) {
            evaluationCriteriaCollectionService.addOrUpdateEvaluationCriteriaCollection(dto);

        }
        return dto;
    }

    @PostMapping("")
    public EvaluationCriteriaCollectionDTO create(
            @RequestBody EvaluationCriteriaCollectionDTO dto) {
        evaluationCriteriaCollectionService.addOrUpdateEvaluationCriteriaCollection(dto);
        return dto;
    }
}
