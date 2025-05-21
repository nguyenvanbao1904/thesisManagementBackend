/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.CommitteeDTO;
import com.nvb.pojo.Committee;
import com.nvb.pojo.CommitteeCampus;
import com.nvb.pojo.CommitteeMember;
import com.nvb.pojo.Thesis;
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
    public String update(Model model, @PathVariable(name = "id") int id) {
        Committee committee = committeeService.getCommittee(Map.of("id", String.valueOf(id)));
        if (committee == null) {
            return "redirect:/committees";
        }
        
        CommitteeDTO committeeDTO = modelMapper.map(committee, CommitteeDTO.class);
        
        // Chuẩn bị dữ liệu cho thành viên hội đồng
        if (committee.getCommitteeMembers() != null && !committee.getCommitteeMembers().isEmpty()) {
            Integer[] memberIds = new Integer[committee.getCommitteeMembers().size()];
            String[] memberRoles = new String[committee.getCommitteeMembers().size()];
            
            int i = 0;
            for (CommitteeMember member : committee.getCommitteeMembers()) {
                memberIds[i] = member.getLecturer().getId();
                String role = member.getRole();
                if (role.startsWith("ROLE_")) {
                    role = role.substring(5);
                }
                memberRoles[i] = role;
                i++;
            }
            
            committeeDTO.setMemberLecturerId(memberIds);
            committeeDTO.setMemberRole(memberRoles);
        }
        
        if (committee.getTheses() != null && !committee.getTheses().isEmpty()) {
            Integer[] thesesIds = new Integer[committee.getTheses().size()];
            
            int i = 0;
            for (Thesis thesis : committee.getTheses()) {
                thesesIds[i] = thesis.getId();
                i++;
            }
            
            committeeDTO.setThesesIds(thesesIds);
        }
        
        model.addAttribute("committee", committeeDTO);
        model.addAttribute("committeeCampus", CommitteeCampus.values());
        model.addAttribute("lecturers", lecturerService.getLecturers(new HashMap<>()));
        
        List<Thesis> unassignedTheses = thesesService.getTheses(new HashMap<>(Map.of("committeeId", "")));
        if (committee.getTheses() != null) {
            unassignedTheses.addAll(committee.getTheses());
        }
        model.addAttribute("unassignedTheses", unassignedTheses);
        
        return "committee/add";
    }

    @PostMapping("/add")
    public String add(Model model,
            @ModelAttribute("committee") CommitteeDTO committeeDTO) {
        committeeService.addOrUpdate(committeeDTO);
        return "redirect:/committees";
    }
}
