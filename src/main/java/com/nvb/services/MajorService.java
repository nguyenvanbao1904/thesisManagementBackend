/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import java.util.List;
import java.util.Map;
import com.nvb.dto.MajorDTO;

/**
 *
 * @author nguyenvanbao
 */
public interface MajorService {
    MajorDTO addOrUpdate(MajorDTO majorDTO);
    List<MajorDTO> getAll(Map<String, String> params);
    MajorDTO get(Map<String, String> params);
    void delete(int id);
}
