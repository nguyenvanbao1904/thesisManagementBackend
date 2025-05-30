/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaCollectionListDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.services.EvaluationCriteriaCollectionService;
import com.nvb.services.EvaluationCriteriaService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nguyenvanbao
 */
@Controller
@RequestMapping("/evaluation_criteria_collections")
public class EvaluationCriteriaCollectionController {

    @Autowired
    private EvaluationCriteriaCollectionService evaluationCriteriaCollectionService;

    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;

    @Autowired
    @Qualifier("evaluationCriteriaCollectionWebAppValidator")
    private WebAppValidator evaluationCriteriaCollectionWebAppValidator;

    @InitBinder()
    public void initEvaluationCriteriaCollectionBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaCollectionWebAppValidator);
    }

    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<EvaluationCriteriaCollectionListDTO> collections = this.evaluationCriteriaCollectionService.getAllForListView(params, true);
        model.addAttribute("collections", collections);
        int page = 1;
        if (params.get("page") != null) {
            try {
                page = Integer.parseInt(params.get("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        model.addAttribute("currentPage", page);
        return "evaluationCriteriaCollection/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        EvaluationCriteriaCollectionDTO eccDto = new EvaluationCriteriaCollectionDTO();
        List<EvaluationCriteriaDTO> allCriteriasFromService = evaluationCriteriaService.getAll(new HashMap<>());
        model.addAttribute("allCriterias", allCriteriasFromService);
        model.addAttribute("evaluationCriteriaCollection", eccDto);
        return "evaluationCriteriaCollection/add";
    }
    
    @PostMapping("/add")
    public String addEvaluationCriteriaCollection(@Valid @ModelAttribute("evaluationCriteriaCollection") EvaluationCriteriaCollectionDTO dto,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {
            List<EvaluationCriteriaDTO> allCriteriasFromService = evaluationCriteriaService.getAll(new HashMap<>());
            model.addAttribute("allCriterias", allCriteriasFromService);
            return "evaluationCriteriaCollection/add";
        }

        evaluationCriteriaCollectionService.addOrUpdate(dto);
        return "redirect:/evaluation_criteria_collections";
    }
    
    @GetMapping("/{id}")
    public String updateView(Model model, @PathVariable(name = "id") int id) {
        EvaluationCriteriaCollectionDTO dto = evaluationCriteriaCollectionService.get(new HashMap<>(Map.of("id", String.valueOf(id))));
        
        if (dto == null) {
            return "error/404";
        }

        List<EvaluationCriteriaDTO> allCriterias = evaluationCriteriaService.getAll(new HashMap<>());
        
        model.addAttribute("allCriterias", allCriterias);
        model.addAttribute("evaluationCriteriaCollection", dto);
        return "evaluationCriteriaCollection/add";
    }
}
