/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.MajorDTO;
import com.nvb.services.MajorService;
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
@RequestMapping("/majors")
public class MajorController {
    @Autowired
    private MajorService majorService;
    
    @Autowired
    @Qualifier("majorWebAppValidator")
    private WebAppValidator majorWebAppValidator;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(majorWebAppValidator);
    }
    
    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<MajorDTO> majors = majorService.getAll(params);
        model.addAttribute("majors", majors);
        return "major/index";
    }

    @GetMapping("/add")
    public String addView(Model model) {
        model.addAttribute("major", new MajorDTO());
        return "major/add";
    }

    @PostMapping("/add")
    public String addMajor(@ModelAttribute("major") @Valid MajorDTO major, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("major", major);
            return "major/add";
        }
        majorService.addOrUpdate(major);
        return "redirect:/majors";
    }
    
    @GetMapping("/{id}")
    public String updateView(Model model, @PathVariable(name = "id") int id){
        model.addAttribute("major", majorService.get(new HashMap<>(Map.of("id", String.valueOf(id)))));
        return "major/add";
    }
}
