/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.EvaluationCriteriaCollectionDetail;
import com.nvb.pojo.Thesis;
import com.nvb.services.ThesesService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/theses")
public class ThesesController {

    @Autowired
    private ThesesService thesesService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("evaluationCriteriaCollectionWebAppValidator")
    private WebAppValidator evaluationCriteriaCollectionWebAppValidator;

    @Autowired
    @Qualifier("evaluationCriteriaWebAppValidator")
    private WebAppValidator evaluationCriteriaWebAppValidator;

    @InitBinder("evaluationCriteriaCollection")
    public void initEvaluationCriteriaCollectionBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaCollectionWebAppValidator);
    }

    @InitBinder("evaluationCriteria")
    public void initEvaluationCriteriaBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaWebAppValidator);
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

    @GetMapping("/evaluation_criteria_collection/{id}")
    public String updateViewEvaluationCriteriaCollection(Model model, @PathVariable(name = "id") int id) {
        EvaluationCriteriaCollection ecc = thesesService.getEvaluationCriteriaCollectionsWithDetails(new HashMap<>(Map.of("id", String.valueOf(id)))).get(0); // Tạo hàm mới nếu chưa có

        EvaluationCriteriaCollectionDTO dto = new EvaluationCriteriaCollectionDTO();
        dto.setId(ecc.getId());
        dto.setName(ecc.getName());
        dto.setDescription(ecc.getDescription());

        List<EvaluationCriteriaDTO> allCriterias = thesesService.getEvaluationCriterias(new HashMap<>());

        List<Integer> selectedIds = new ArrayList<>();
        Map<Integer, Float> weights = new HashMap<>();

        for (EvaluationCriteriaCollectionDetail detail : ecc.getEvaluationCriteriaCollectionDetails()) {
            EvaluationCriteria crit = detail.getEvaluationCriteria();
            selectedIds.add(crit.getId());
            weights.put(crit.getId(), detail.getWeight()); // map id với weight
        }

        for (EvaluationCriteriaDTO dtoItem : allCriterias) {
            if (selectedIds.contains(dtoItem.getId())) {
                dtoItem.setWeight(weights.get(dtoItem.getId()));
            } else {
                dtoItem.setWeight(null);
            }
        }

        dto.setSelectedCriteriaIds(selectedIds);
        dto.setEvaluationCriterias(allCriterias);

        model.addAttribute("evaluationCriteriaCollection", dto);
        return "theses/evaluationCriteriaCollectionAdd";
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
        thesesService.addOrUpdateEvaluationCriteria(evaluationCriteria);
        return "redirect:/theses/evaluation_criterias";
    }

    @GetMapping("/evaluation_criterias/{id}")
    public String updateViewEvaluationCriterias(Model model, @PathVariable(name = "id") int id) {
        model.addAttribute("evaluationCriteria", thesesService.getEvaluationCriterias(new HashMap<>(Map.of("id", String.valueOf(id)))).get(0));
        return "theses/evaluationCriteriaAdd";
    }
}
