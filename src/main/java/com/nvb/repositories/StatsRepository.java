/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import java.util.List;

/**
 *
 * @author nguyenvanbao
 */
public interface StatsRepository {

    List<Object[]> statsThesisScoresByYear(int year);

    List<Object[]> statsThesisParticipationByMajor(int year);
}
