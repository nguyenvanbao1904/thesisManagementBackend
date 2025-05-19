/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.ThesesDTO;
import com.nvb.pojo.Thesis;
import java.util.List;
import java.util.Map;


/**
 *
 * @author nguyenvanbao
 */
public interface ThesesService {
    List<Thesis> getTheses(Map<String, String> params);
    List<Thesis> getTheses(Map<String, String> params, boolean pagination);
    Thesis addOrUpdate(ThesesDTO thesesDTO);
}
