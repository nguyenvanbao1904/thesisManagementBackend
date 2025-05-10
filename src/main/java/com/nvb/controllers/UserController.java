/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.User;
import com.nvb.services.MajorService;
import com.nvb.services.UserService;
import com.nvb.validators.WebAppValidator;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nguyenvanbao
 */
@Controller
public class UserController {

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private MajorService majorService;

    @Autowired
    @Qualifier("userWebAppValidator")
    private WebAppValidator userWebAppValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(userWebAppValidator);
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam Map<String, String> params) {
        String error = params.get("error");
        if (error != null) {
            model.addAttribute("error", "Login fail");
        }
        return "users/login";
    }

    @GetMapping("/users")
    public String showAll(Model model, @RequestParam(required = false) Map<String, String> params) {
        List<User> users = userDetailsService.getUsers(params);
        model.addAttribute("users", users);
        int page = 1;
        if (params.get("page") != null) {
            try {
                if (users.isEmpty()) {
                    page = 0;
                } else {
                    page = Integer.parseInt(params.get("page"));

                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        model.addAttribute("page", page);
        return "users/index";
    }

    @GetMapping("/users/add")
    public String addView(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("majors", majorService.getMajors(null));
        return "users/add";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("user") @Valid UserDTO user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("majors", majorService.getMajors(null));
            return "users/add";
        }

        Map<String, String> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("phone", user.getPhone());
        params.put("role", user.getRole());
        params.put("studentId", user.getStudentId());
        if (user.getMajorId() != null) {
            params.put("majorId", user.getMajorId().toString());
        }
        params.put("academicTitle", user.getAcademicTitle());
        params.put("academicDegree", user.getAcademicDegree());

        userDetailsService.addUser(params, user.getFile());

        return "redirect:/users";
    }

}
