/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.LecturerAssignmentsDTO;

/**
 *
 * @author nguyenvanbao
 */
public interface LecturerService {
    LecturerAssignmentsDTO getLecturerAssignments(Integer lecturerId);
}
