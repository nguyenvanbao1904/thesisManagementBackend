/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.LecturerAssignmentsDTO;
import com.nvb.dto.UserDTO;
import com.nvb.services.LecturerService;
import com.nvb.services.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyenvanbao
 */
@RestController
@RequestMapping("/api/lecturer")
@CrossOrigin
public class ApiLecturerController {

    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private LecturerService lecturerService;

    @GetMapping("/assignments")
    public ResponseEntity<LecturerAssignmentsDTO> getLecturerAssignments(Principal principal) {
        UserDTO currentUser = this.userDetailsService.get(new HashMap<>(Map.of("username", principal.getName())));
        LecturerAssignmentsDTO assignments = lecturerService.getLecturerAssignments(currentUser.getId());
        return ResponseEntity.ok(assignments);
    }

}
