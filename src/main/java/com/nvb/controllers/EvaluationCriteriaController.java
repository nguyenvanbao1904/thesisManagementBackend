/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaDTO;
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
@RequestMapping("/evaluation_criterias")
public class EvaluationCriteriaController {

    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;
    
    @Autowired
    @Qualifier("evaluationCriteriaWebAppValidator")
    private WebAppValidator evaluationCriteriaWebAppValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaWebAppValidator);
    }

    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<EvaluationCriteriaDTO> evaluationCriterias = evaluationCriteriaService.getAll(params, true);
        
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
        return "evaluationCriteria/index";
    }
    
    @GetMapping("/add")
    public String addView(Model model) {
        model.addAttribute("evaluationCriteria", new EvaluationCriteriaDTO());
        return "evaluationCriteria/add";
    }
    
    @PostMapping("/add")
    public String addEvaluationCriteria(Model model,
            @ModelAttribute("evaluationCriteria") @Valid EvaluationCriteriaDTO evaluationCriteria,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("evaluationCriteria", evaluationCriteria);
            return "evaluationCriteria/add";
        }
        evaluationCriteriaService.addOrUpdate(evaluationCriteria);
        return "redirect:/evaluation_criterias";
    }
    
    @GetMapping("/{id}")
    public String updateView(Model model, @PathVariable(name = "id") int id) {
        EvaluationCriteriaDTO criteria = evaluationCriteriaService.get(new HashMap<>(Map.of("id", String.valueOf(id))));
        model.addAttribute("evaluationCriteria", criteria);
        return "evaluationCriteria/add";
    }
}
