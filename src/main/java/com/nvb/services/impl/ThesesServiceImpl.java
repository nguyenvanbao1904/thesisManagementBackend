/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.pojo.Thesis;
import com.nvb.repositories.ThesesRepository;
import com.nvb.services.ThesesService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Transactional
@Service
public class ThesesServiceImpl implements ThesesService {

    @Autowired
    private ThesesRepository thesesRepository;

    @Override
    public List<Thesis> getTheses(Map<String, String> params) {
        return this.getTheses(params, false);
    }

    @Override
    public List<Thesis> getTheses(Map<String, String> params, boolean pagination) {
        return thesesRepository.getTheses(params, pagination);
    }
}
