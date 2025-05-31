/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Committee;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface CommitteeRepository {
    List<Committee> getAll(Map<String, String> params, boolean pagination, boolean details);
    Committee get(Map<String, String> params);
    Committee addOrUpdate(Committee committee);
    void delete(int id);
    
    // schedule 
    List<Committee> findLockedCommitteesInTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    List<Committee> findActiveCommitteesAfterDefense(LocalDateTime lockTime);
    List<Committee> findMissedCommittees(LocalDateTime pastTime, LocalDateTime now);
    List<Committee> findOverdueCommittees(LocalDateTime lockTime);
}
