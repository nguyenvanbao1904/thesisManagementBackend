/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.pojo.AcademicStaff;
import com.nvb.repositories.AcademicsStaffRepository;
import com.nvb.services.AcademicsStaffService;
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
public class AcademicsStaffServiceImpl implements AcademicsStaffService{
    
    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private AcademicsStaffRepository academicsStaffRepository;

    @Override
    public AcademicStaff addAcademicStaff(Map<String, String> params) {
        AcademicStaff academicStaff = new AcademicStaff();
        academicStaff.setUser(userDetailsService.getUserByUsername(params.get("username")));
        return academicsStaffRepository.addAcademicStaff(academicStaff);
    }
    
}
