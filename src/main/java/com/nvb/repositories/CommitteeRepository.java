/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Committee;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface CommitteeRepository {
    List<Committee> getCommittees(Map<String, String> params, boolean pagination, boolean details);
    Committee getCommittee(Map<String, String> params);
    Committee addOrUpdate(Committee committee);
    void deleteCommittee(int id);
}
