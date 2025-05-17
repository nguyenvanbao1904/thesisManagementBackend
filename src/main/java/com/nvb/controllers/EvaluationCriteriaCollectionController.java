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
import com.nvb.services.EvaluationCriteriaCollectionService;
import com.nvb.services.EvaluationCriteriaService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("evaluationCriteriaCollectionWebAppValidator")
    private WebAppValidator evaluationCriteriaCollectionWebAppValidator;

    @InitBinder()
    public void initEvaluationCriteriaCollectionBinder(WebDataBinder binder) {
        binder.setValidator(evaluationCriteriaCollectionWebAppValidator);
    }

    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<EvaluationCriteriaCollection> evaluationCriteriaCollections = evaluationCriteriaCollectionService.getEvaluationCriteriaCollections(params, true);
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
        return "evaluationCriteriaCollection/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        EvaluationCriteriaCollectionDTO eccDto = new EvaluationCriteriaCollectionDTO();
        List<EvaluationCriteriaDTO> allCriteriasFromService = modelMapper.map(
                evaluationCriteriaService.getEvaluationCriterias(new HashMap<>()),
                new TypeToken<List<EvaluationCriteriaDTO>>() {
                }.getType()
        );
        eccDto.setEvaluationCriterias(allCriteriasFromService);
        model.addAttribute("evaluationCriteriaCollection", eccDto);
        return "evaluationCriteriaCollection/add";
    }
    
    @PostMapping("/add")
    public String addEvaluationCriteriaCollection(@Valid @ModelAttribute("evaluationCriteriaCollection") EvaluationCriteriaCollectionDTO dto,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {
            // Load lại tất cả tiêu chí để hiện lại view
            dto.setEvaluationCriterias(modelMapper.map(
                evaluationCriteriaService.getEvaluationCriterias(new HashMap<>()),
                new TypeToken<List<EvaluationCriteriaDTO>>() {
                }.getType()
        ));
            return "evaluationCriteriaCollection/add";
        }

        // Lưu DTO thông qua service
        evaluationCriteriaCollectionService.addOrUpdateEvaluationCriteriaCollection(dto);
        return "redirect:/evaluation_criteria_collections";
    }
    
    @GetMapping("/{id}")
    public String updateView(Model model, @PathVariable(name = "id") int id) {
        EvaluationCriteriaCollection ecc = evaluationCriteriaCollectionService.getEvaluationCriteriaCollectionsWithDetails(new HashMap<>(Map.of("id", String.valueOf(id)))).get(0); // Tạo hàm mới nếu chưa có

        EvaluationCriteriaCollectionDTO dto = new EvaluationCriteriaCollectionDTO();
        dto.setId(ecc.getId());
        dto.setName(ecc.getName());
        dto.setDescription(ecc.getDescription());

        List<EvaluationCriteriaDTO> allCriterias = modelMapper.map(
                evaluationCriteriaService.getEvaluationCriterias(new HashMap<>()),
                new TypeToken<List<EvaluationCriteriaDTO>>() {
                }.getType()
        );

        List<EvaluationCriteria> selecteds = new ArrayList<>();
        Map<Integer, Float> weights = new HashMap<>();

        for (EvaluationCriteriaCollectionDetail detail : ecc.getEvaluationCriteriaCollectionDetails()) {
            EvaluationCriteria crit = detail.getEvaluationCriteria();
            selecteds.add(crit);
            weights.put(crit.getId(), detail.getWeight()); // map id với weight
        }

        for (EvaluationCriteriaDTO dtoItem : allCriterias) {
            EvaluationCriteria criteria = selecteds.stream().filter(s -> s.getId().equals(dtoItem.getId())).findFirst().orElse(null);
            if (criteria != null) {
                dtoItem.setWeight(weights.get(dtoItem.getId()));
            } else {
                dtoItem.setWeight(null);
            }
        }

        dto.setSelectedCriterias(selecteds);
        dto.setEvaluationCriterias(allCriterias);

        model.addAttribute("evaluationCriteriaCollection", dto);
        return "evaluationCriteriaCollection/add";
    }
}
