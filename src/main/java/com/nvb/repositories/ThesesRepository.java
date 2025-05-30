/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Thesis;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface ThesesRepository {

    List<Thesis> getAll(Map<String, String> params, boolean pagination, boolean details);

    Thesis addOrUpdate(Thesis thesis);

    Thesis get(Map<String, String> params);

    void delete(int id);

    int countThesesByStudentAndNotThisThesis(Integer studentUserId, Integer thesisIdToExclude);

    List<Thesis> getByIds(List<Integer> ids);
}
