/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Admin;
import com.nvb.pojo.User;
import com.nvb.repositories.AdminRepository;
import com.nvb.services.AdminService;
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
    private AdminRepository adminRepository;
    
    @Override
    public Admin prepareAdmin(User user, UserDTO userDto) {
        Admin admin;
        if(user.getAdmin() != null){
            admin = user.getAdmin();
        }else{
            admin = new Admin();
            admin.setUser(user);
        }
        return admin;
    }
}
