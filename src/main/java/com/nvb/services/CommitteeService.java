/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.CommitteeDTO;
import com.nvb.dto.CommitteeListDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface CommitteeService {

    List<CommitteeDTO> getAll(Map<String, String> params);

    List<CommitteeDTO> getAll(Map<String, String> params, boolean pagination);

    List<CommitteeDTO> getAll(Map<String, String> params, boolean pagination, boolean details);

    List<CommitteeListDTO> getAllForListView(Map<String, String> params, boolean pagination);

    CommitteeDTO get(Map<String, String> params);

    CommitteeDTO addOrUpdate(CommitteeDTO committeeDTO);
    
    CommitteeDTO updateStatus(CommitteeDTO existing, String status);

    void delete(int id);
    
    // schedule 
    int activateCommitteesBeforeDefense(Date now, Date targetTime);

    int lockCommitteesAfterDefense(Date locktime);

    int activateMissedCommittees(Date now, Date futureTime);

    int lockOverdueCommittees(Date lockTime);
}
