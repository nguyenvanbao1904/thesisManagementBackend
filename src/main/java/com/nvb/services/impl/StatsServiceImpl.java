/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.repositories.StatsRepository;
import com.nvb.services.StatsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class StatsServiceImpl implements StatsService{
    
    @Autowired
    private StatsRepository statsRepository;

    @Override
    public List<Object[]> statsThesisScoresByYear(int year) {
        return statsRepository.statsThesisScoresByYear(year);
    }

    @Override
    public List<Object[]> statsThesisParticipationByMajor(int year) {
        return statsRepository.statsThesisParticipationByMajor(year);
    }
    
}
