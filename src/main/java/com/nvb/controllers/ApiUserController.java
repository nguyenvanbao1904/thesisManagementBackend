/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.UserDTO;
import com.nvb.services.UserService;
import com.nvb.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyenvanbao
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {
    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private JwtUtils jwtUtils; 
    
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        userDetailsService.delete(id);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO u) {
        UserDTO userDto = this.userDetailsService.authenticate(u.getUsername(), u.getPassword());
        if (userDto != null) {
            try {
                String token = jwtUtils.generateToken(userDto.getUsername(), userDto.getRole());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }
    
    @RequestMapping("/secure/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<UserDTO> getProfile(Principal principal) {
        UserDTO user = this.userDetailsService.get(Map.of("username", principal.getName()));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping("users")
    public ResponseEntity<List<UserDTO>> list(@RequestParam Map<String, String> params){
        List<UserDTO> collections = this.userDetailsService.getAll(params, true);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }
}
