/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.CommitteeDTO;
import com.nvb.pojo.Committee;
import com.nvb.pojo.CommitteeCampus;
import com.nvb.services.CommitteeService;
import com.nvb.services.LecturerService;
import com.nvb.services.ThesesService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/committees")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private ThesesService thesesService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<Committee> committees = committeeService.getCommittees(new HashMap<>(), true, true);
        model.addAttribute("committees", committees);
        int page = 1;
        if (params.get("page") != null) {
            try {
                if (committees.isEmpty()) {
                    page = 0;
                } else {
                    page = Integer.parseInt(params.get("page"));
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        model.addAttribute("page", page);
        return "committee/index";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("committee", new CommitteeDTO());

        model.addAttribute("committeeCampus", CommitteeCampus.values());

        model.addAttribute("lecturers", lecturerService.getLecturers(new HashMap<>()));

        model.addAttribute("unassignedTheses", thesesService.getTheses(new HashMap<>(Map.of("committeeId", ""))));

        return "committee/add";
    }
    
    @GetMapping("/{id}")
    public String getCommittee(@PathVariable Integer id, Model model) {
        Committee committee = committeeService.getCommittee(Map.of("id", id.toString()));
        if (committee == null) {
            return "redirect:/committees";
        }
        
        // Chuyển từ entity sang DTO
        CommitteeDTO committeeDTO = modelMapper.map(committee, CommitteeDTO.class);
        
        model.addAttribute("committee", committeeDTO);
        model.addAttribute("committeeCampus", CommitteeCampus.values());
        model.addAttribute("lecturers", lecturerService.getLecturers(new HashMap<>()));
        model.addAttribute("unassignedTheses", thesesService.getTheses(new HashMap<>(Map.of("committeeId", ""))));
        
        return "committee/add";
    }

    @PostMapping("/add")
    public String add(Model model,
            @ModelAttribute("committee") CommitteeDTO committeeDTO) {
        committeeService.addOrUpdate(committeeDTO);
        return "redirect:/committees";
    }
}
