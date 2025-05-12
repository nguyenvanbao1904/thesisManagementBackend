/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.pojo.Major;
import com.nvb.services.MajorService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        model.addAttribute("majors", majorService.getMajors(params));
        return "major/index";
    }

    @GetMapping("/add")
    public String addView(Model model) {
        model.addAttribute("major", new Major());
        return "major/add";
    }

    @PostMapping("/add")
    public String addMajor(@ModelAttribute("major") Major major) {
        majorService.addOrUpdateMajor(major);
        return "redirect:/majors";
    }
    
    @GetMapping("/{id}")
    public String updateView(Model model, @PathVariable(name = "id") int id){
        model.addAttribute("major", majorService.getMajorById(id));
        return "major/add";
    }
}
