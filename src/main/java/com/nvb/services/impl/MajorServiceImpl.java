/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.pojo.Major;
import com.nvb.repositories.MajorRepository;
import com.nvb.services.MajorService;
import java.util.List;
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
public class MajorServiceImpl implements MajorService{

    @Autowired
    private MajorRepository majorRepository;
    
    @Override
    public Major addMajor(Major major) {
        return majorRepository.addMajor(major);
    }

    @Override
    public List<Major> getMajors(Map<String, String> params) {
        return majorRepository.getMajors(params);
    }
    
}
