/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.AcademicStaff;
import com.nvb.repositories.AcademicsStaffRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Repository
@Transactional
public class AcademicsStaffRepositoryImpl implements AcademicsStaffRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public AcademicStaff addAcademicStaff(AcademicStaff academicStaff) {
        Session s = factory.getObject().getCurrentSession();
        s.persist(academicStaff);
        return academicStaff;
    }
    
}
