/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.pojo.User;
import com.nvb.services.UserService;
import java.util.HashMap;
import org.springframework.ui.Model;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nguyenvanbao
 */
@Controller
public class UserController {

    @Autowired
    private UserService userDetailsService;

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
        model.addAttribute("users", userDetailsService.getUsers(params));
        return "users/index";
    }

    @GetMapping("/users/add")
    public String addView(Model model) {
        model.addAttribute("user", new User());
        return "users/add";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("user") User user,
            @RequestParam("file") MultipartFile avatar,
            Model model) {
        Map<String, String> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("phone", user.getPhone());
        params.put("role", user.getRole());

        userDetailsService.addUser(params, avatar);

        return "redirect:/users";
    }

}
