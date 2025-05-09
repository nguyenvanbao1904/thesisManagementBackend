/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.Lecturer;
import com.nvb.repositories.LecturerRepository;
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
public class LecturerRepositoryImpl implements LecturerRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Lecturer addLecturer(Lecturer lecturer) {
        Session s = factory.getObject().getCurrentSession();
        s.persist(lecturer);
        return lecturer;
    }
}
