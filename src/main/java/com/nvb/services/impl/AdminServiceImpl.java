/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.pojo.Admin;
import com.nvb.repositories.AdminRepository;
import com.nvb.services.AdminService;
import com.nvb.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService{
    
    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin addAdmin(Map<String, String> params) {
        Admin admin = new Admin();
        admin.setUser(userDetailsService.getUser(Map.of("username", params.get("username"))));
        return adminRepository.addAdmin(admin);
        
    }
    
}
