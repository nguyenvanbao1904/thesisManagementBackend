/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.ThesesDTO;
import com.nvb.dto.ThesesListDTO;
import com.nvb.pojo.Thesis;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface ThesesService {

    List<ThesesDTO> getAll(Map<String, String> params);

    List<ThesesDTO> getAll(Map<String, String> params, boolean pagination);

    List<ThesesDTO> getAll(Map<String, String> params, boolean pagination, boolean details);

    List<ThesesListDTO> getAllForListView(Map<String, String> params, boolean pagination);

    ThesesDTO addOrUpdate(ThesesDTO thesesDTO);

    ThesesDTO get(Map<String, String> params);

    void delete(int id);

    boolean isStudentInAnotherActiveThesis(Integer studentUserId, Integer currentThesisIdToExclude);
    
    List<ThesesDTO> getByIds(List<Integer> ids);
    
    ThesesDTO toDTO(Thesis thesis);
    
    void changeStatus(int thesisId, String thesisStatus);
}
