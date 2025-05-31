/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.CommitteeMemberDTO;
import com.nvb.dto.LecturerAssignmentsDTO;
import com.nvb.dto.ThesesDTO;
import com.nvb.pojo.Lecturer;
import com.nvb.repositories.UserRepository;
import com.nvb.services.LecturerService;
import com.nvb.services.ThesesService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThesesService thesesService;

    @Override
    public LecturerAssignmentsDTO getLecturerAssignments(Integer lecturerId) {
        Lecturer lecturer = (Lecturer) userRepository.get(new HashMap<>(Map.of("id", lecturerId.toString())));
        Set<ThesesDTO> supervisorTheses = lecturer.getThesesSupervisors()
                .stream()
                .map(item -> thesesService.toDTO(item))
                .collect(Collectors.toSet());

        Set<ThesesDTO> reviewerTheses = lecturer.getThesesReviewer()
                .stream()
                .map(item -> thesesService.toDTO(item))
                .collect(Collectors.toSet());
        
        Set<CommitteeMemberDTO> committeeMembers = lecturer.getCommitteeMembers()
                .stream()
                .map(item -> {
                    CommitteeMemberDTO committeeMemberDTO = new CommitteeMemberDTO();
                    committeeMemberDTO.setAcademicDegree(lecturer.getAcademicDegree());
                    committeeMemberDTO.setAcademicTitle(lecturer.getAcademicTitle());
                    committeeMemberDTO.setCommitteeId(item.getCommittee().getId());
                    committeeMemberDTO.setLecturerId(lecturer.getId());
                    committeeMemberDTO.setLecturerName(lecturer.getFirstName());
                    committeeMemberDTO.setRole(item.getRole());
                    return committeeMemberDTO;
                })
                .collect(Collectors.toSet());

        LecturerAssignmentsDTO rs = new LecturerAssignmentsDTO();
        rs.setSupervisorTheses(supervisorTheses);
        rs.setReviewerTheses(reviewerTheses);
        rs.setCommitteeMembers(committeeMembers);
        return rs;
    }

}
