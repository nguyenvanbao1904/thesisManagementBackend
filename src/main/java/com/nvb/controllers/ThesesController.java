/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.Thesis;
import com.nvb.services.ThesesService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nguyenvanbao
 */
@Controller
@RequestMapping("/theses")
public class ThesesController {

    @Autowired
    private ThesesService thesesService;
    
    @Autowired
    @Qualifier("evaluationCriteriaCollectionWebAppValidator")
    private WebAppValidator evaluationCriteriaCollectionWebAppValidator;
    
    @InitBinder("evaluationCriteriaCollection")
    public void initEvaluationCriteriaCollectionBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaCollectionWebAppValidator);
    }
    
    @GetMapping("")
    public String showAllTheses(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<Thesis> theses = thesesService.getTheses(params);
        model.addAttribute("theses", theses);
        int page = 1;
        if (params.get("page") != null) {
            try {
                if (theses.isEmpty()) {
                    page = 0;
                } else {
                    page = Integer.parseInt(params.get("page"));

                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        model.addAttribute("page", page);
        return "theses/index";
    }

    @GetMapping("/evaluation_criteria_collection")
    public String showAllEvaluationCriteriaCollection(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<EvaluationCriteriaCollection> evaluationCriteriaCollections = thesesService.getEvaluationCriteriaCollections(params);
        model.addAttribute("evaluationCriteriaCollections", evaluationCriteriaCollections);
        int page = 1;
        if (params.get("page") != null) {
            try {
                if (evaluationCriteriaCollections.isEmpty()) {
                    page = 0;
                } else {
                    page = Integer.parseInt(params.get("page"));

                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        model.addAttribute("page", page);
        return "theses/evaluationCriteriaCollection";
    }

    @GetMapping("/evaluation_criteria_collection/add")
    public String showAddEvaluationCriteriaCollectionForm(Model model) {
        EvaluationCriteriaCollectionDTO eccDto = new EvaluationCriteriaCollectionDTO();
        List<EvaluationCriteriaDTO> allCriteriasFromService = thesesService.getEvaluationCriterias(new HashMap<>()); 
        eccDto.setEvaluationCriterias(allCriteriasFromService); 
        model.addAttribute("evaluationCriteriaCollection", eccDto);
        return "theses/evaluationCriteriaCollectionAdd";
    }

    @PostMapping("/evaluation_criteria_collection/add")
    public String addEvaluationCriteriaCollection(@Valid @ModelAttribute("evaluationCriteriaCollection") EvaluationCriteriaCollectionDTO dto,
        BindingResult result,
        Model model
    ) {
        
        if (result.hasErrors()) {
            // Load lại tất cả tiêu chí để hiện lại view
            dto.setEvaluationCriterias(thesesService.getEvaluationCriterias(new HashMap<>()));
            return "theses/evaluationCriteriaCollectionAdd";
        }  

        // Lưu DTO thông qua service
        thesesService.addEvaluationCriteriaCollection(dto);
        return "redirect:/theses/evaluation_criteria_collection"; 
    }

    @GetMapping("/evaluation_criterias")
    public String showAllEvaluationCriterias(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<EvaluationCriteriaDTO> evaluationCriterias = thesesService.getEvaluationCriterias(params);
        model.addAttribute("evaluationCriterias", evaluationCriterias);
        int page = 1;
        if (params.get("page") != null) {
            try {
                if (evaluationCriterias.isEmpty()) {
                    page = 0;
                } else {
                    page = Integer.parseInt(params.get("page"));
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        model.addAttribute("page", page);
        return "theses/evaluationCriterias";
    }

    @GetMapping("/evaluation_criterias/add")
    public String addEvaluationCriteriaView(Model model) {
        model.addAttribute("evaluationCriteria", new EvaluationCriteriaDTO());
        return "theses/evaluationCriteriaAdd";
    }

    @PostMapping("/evaluation_criterias/add")
    public String addEvaluationCriteria(Model model,
            @ModelAttribute("evaluationCriteria") @Valid EvaluationCriteriaDTO evaluationCriteria,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("evaluationCriteria", evaluationCriteria);
            return "theses/evaluationCriteriaAdd";
        }
        thesesService.addEvaluationCriteria(evaluationCriteria);
        return "redirect:/theses/evaluation_criterias";
    }
}
