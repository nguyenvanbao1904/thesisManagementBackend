/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.services.StatsService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nguyenvanbao
 */
@Controller
public class IndexController {

    @Autowired
    private StatsService statsService;

    @RequestMapping("/")
    public String index(Model model, @RequestParam(required = false) Map<String, String> params) {
        if (params != null) {
            String year = params.get("year");
            if (year != null && !year.isEmpty()) {
                model.addAttribute("statsThesisParticipationByMajor", statsService.statsThesisParticipationByMajor(Integer.parseInt(year)));
                model.addAttribute("statsThesisScoresByYear", statsService.statsThesisScoresByYear(Integer.parseInt(year)));

            }
        }
        return "index";
    }
}
