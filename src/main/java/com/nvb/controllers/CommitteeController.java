/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.CommitteeDTO;
import com.nvb.dto.CommitteeListDTO;
import com.nvb.dto.ThesesDTO;
import com.nvb.pojo.CommitteeCampus;
import com.nvb.dto.CommitteeMemberDTO;
import com.nvb.services.CommitteeService;
import com.nvb.services.UserService;
import com.nvb.services.ThesesService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    private UserService userService;

    @Autowired
    private ThesesService thesesService;

    @Autowired
    @Qualifier("committeeWebAppValidator")
    private WebAppValidator committeeWebAppValidator;

    @InitBinder()
    public void initCommitteeWebAppValidatorBinder(WebDataBinder binder) {
        binder.setValidator(committeeWebAppValidator);
    }

    @GetMapping("")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<CommitteeListDTO> committees = committeeService.getAllForListView(new HashMap<>(), true);
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
        model.addAttribute("lecturers", userService.getAll(new HashMap<>(Map.of("role", "ROLE_LECTURER"))));
        model.addAttribute("unassignedTheses", thesesService.getAll(new HashMap<>(Map.of("committeeId", ""))));
        return "committee/add";
    }

    @GetMapping("/{id}")
    public String update(Model model, @PathVariable(name = "id") int id) {
        CommitteeDTO committeeDTO = committeeService.get(Map.of("id", String.valueOf(id)));
        if (committeeDTO == null) {
            return "redirect:/committees";
        }
        if (committeeDTO.getCommitteeMembers() != null && !committeeDTO.getCommitteeMembers().isEmpty()) {
            Integer[] memberIds = new Integer[committeeDTO.getCommitteeMembers().size()];
            String[] memberRoles = new String[committeeDTO.getCommitteeMembers().size()];

            int i = 0;
            for (CommitteeMemberDTO member : committeeDTO.getCommitteeMembers()) {
                memberIds[i] = member.getLecturerId();
                String role = member.getRole();
                if (role != null && role.startsWith("ROLE_")) {
                    role = role.substring(5);
                }
                memberRoles[i] = role;
                i++;
            }

            committeeDTO.setMemberLecturerId(memberIds);
            committeeDTO.setMemberRole(memberRoles);
        }

        if (committeeDTO.getTheses() != null && !committeeDTO.getTheses().isEmpty()) {
            Integer[] thesesIds = new Integer[committeeDTO.getTheses().size()];

            int i = 0;
            for (ThesesDTO thesis : committeeDTO.getTheses()) {
                thesesIds[i] = thesis.getId();
                i++;
            }

            committeeDTO.setThesesIds(thesesIds);
        }

        List<ThesesDTO> availableTheses = thesesService.getAll(new HashMap<>(Map.of("committeeId", "")));

        if (committeeDTO.getThesesIds() != null && committeeDTO.getThesesIds().length != 0) {

            // Chuan bi map Thesis
            Set<Integer> dtoThesesIds = Optional.ofNullable(committeeDTO.getThesesIds())
                    .map(ids -> Arrays.stream(ids).filter(Objects::nonNull).collect(Collectors.toSet()))
                    .orElse(Collections.emptySet());

            Map<Integer, ThesesDTO> thesesMap = dtoThesesIds.isEmpty() ? new HashMap<>()
                    : thesesService.getByIds(new ArrayList<>(dtoThesesIds)).stream()
                            .collect(Collectors.toMap(ThesesDTO::getId, t -> t));

            for (int thesisId : committeeDTO.getThesesIds()) {
                if (!availableTheses.stream().anyMatch(t -> t.getId().equals(thesisId))) {
                    availableTheses.add(thesesMap.get(thesisId));
                }
            }
        }
        model.addAttribute("committee", committeeDTO);
        model.addAttribute("committeeCampus", CommitteeCampus.values());
        model.addAttribute("lecturers", userService.getAll(new HashMap<>(Map.of("role", "ROLE_LECTURER"))));
        model.addAttribute("unassignedTheses", availableTheses);
        return "committee/add";
    }

    @PostMapping("/add")
    public String add(Model model,
            @Valid @ModelAttribute("committee") CommitteeDTO committeeDTO,
            BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("committee", committeeDTO);
            model.addAttribute("committeeCampus", CommitteeCampus.values());
            model.addAttribute("lecturers", userService.getAll(new HashMap<>(Map.of("role", "ROLE_LECTURER"))));
            model.addAttribute("unassignedTheses", thesesService.getAll(new HashMap<>(Map.of("committeeId", ""))));
            return "committee/add";
        }
        committeeService.addOrUpdate(committeeDTO);
        return "redirect:/committees";
    }
}
