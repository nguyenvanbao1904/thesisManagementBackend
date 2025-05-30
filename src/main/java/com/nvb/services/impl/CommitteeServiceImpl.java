/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.CommitteeDTO;
import com.nvb.dto.CommitteeListDTO;
import com.nvb.dto.CommitteeMemberDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Committee;
import com.nvb.pojo.CommitteeMember;
import com.nvb.pojo.CommitteeMemberPK;
import com.nvb.pojo.CommitteeMemberRole;
import com.nvb.pojo.CommitteeStatus;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Thesis;
import com.nvb.repositories.AcademicsStaffRepository;
import com.nvb.repositories.CommitteeRepository;
import com.nvb.repositories.LecturerRepository;
import com.nvb.repositories.ThesesRepository;
import com.nvb.services.CommitteeService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class CommitteeServiceImpl implements CommitteeService {

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private AcademicsStaffRepository academicsStaffRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private ThesesRepository thesesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CommitteeDTO> getAll(Map<String, String> params) {
        return getAll(params, false, false);
    }

    @Override
    public List<CommitteeDTO> getAll(Map<String, String> params, boolean pagination) {
        return getAll(params, pagination, false);
    }

    @Override
    public List<CommitteeDTO> getAll(Map<String, String> params, boolean pagination, boolean details) {
        return committeeRepository.getAll(params, pagination, details)
                .stream().map(this::toCommitteeDTO).collect(Collectors.toList());
    }

    @Override
    public List<CommitteeListDTO> getAllForListView(Map<String, String> params, boolean pagination) {
        return committeeRepository.getAll(params, pagination, false)
                .stream().map(this::toCommitteeListDTO).collect(Collectors.toList());
    }

    @Override
    public CommitteeDTO get(Map<String, String> params) {
        Committee committee = committeeRepository.get(params);
        return committee != null ? toCommitteeDTO(committee) : null;
    }

    private CommitteeMemberRole parseRole(String roleStr) {
        if (roleStr == null) {
            throw new IllegalArgumentException("Chuỗi vai trò không được null");
        }
        switch (roleStr.toUpperCase()) {
            case "CHAIRMAN":
                return CommitteeMemberRole.ROLE_CHAIRMAN;
            case "SECRETARY":
                return CommitteeMemberRole.ROLE_SECRETARY;
            case "REVIEWER":
                return CommitteeMemberRole.ROLE_REVIEWER;
            case "MEMBER":
                return CommitteeMemberRole.ROLE_MEMBER;
            default:
                return CommitteeMemberRole.valueOf(roleStr.toUpperCase());
        }
    }

    @Override
    public CommitteeDTO addOrUpdate(CommitteeDTO committeeDTO) {
        Committee committee;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff academicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffRepository.get(Map.of("username", authentication.getName()));
        }

        // Chuẩn bị map Lecturer
        Set<Integer> dtoLecturerIds = Optional.ofNullable(committeeDTO.getMemberLecturerId())
                .map(ids -> Arrays.stream(ids).filter(Objects::nonNull).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Map<Integer, Lecturer> lecturerMap = dtoLecturerIds.isEmpty() ? new HashMap<>()
                : lecturerRepository.getByIds(new ArrayList<>(dtoLecturerIds)).stream()
                        .collect(Collectors.toMap(Lecturer::getId, l -> l));

        // Chuan bi map Thesis
        Set<Integer> dtoThesesIds = Optional.ofNullable(committeeDTO.getThesesIds())
                .map(ids -> Arrays.stream(ids).filter(Objects::nonNull).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Map<Integer, Thesis> thesesMap = dtoThesesIds.isEmpty() ? new HashMap<>()
                : thesesRepository.getByIds(new ArrayList<>(dtoThesesIds)).stream()
                        .collect(Collectors.toMap(Thesis::getId, t -> t));

        if (committeeDTO.getId() == null) {

            committee = new Committee();
            committee.setStatus(CommitteeStatus.LOCKED.toString());
            committee.setCommitteeMembers(new HashSet<>());
            committee.setTheses(new HashSet<>());
            committee.setIsActive(true);

        } else {
            committee = committeeRepository.get(Map.of("id", String.valueOf(committeeDTO.getId())));
            if (committee == null) {
                throw new EntityNotFoundException("Không tìm thấy hội đồng với ID: " + committeeDTO.getId());
            }

            // Xóa các thành viên cũ khỏi collection
            committee.getCommitteeMembers().clear();

            // Xóa các thesis khỏi collection và cập nhật tham chiếu
            if (committee.getTheses() != null) {
                committee.getTheses().forEach(thesis -> thesis.setCommitteeId(null));
                committee.getTheses().clear();
            }
        }

        committee.setLocation(committeeDTO.getLocation());
        committee.setCreatedBy(academicStaff);
        committee.setDefenseDate(committeeDTO.getDefenseDate());

        // Lưu Committee trước để có ID
        committee = committeeRepository.addOrUpdate(committee);

        if (committeeDTO.getMemberLecturerId() != null && committeeDTO.getMemberRole() != null) {
            for (int i = 0; i < committeeDTO.getMemberLecturerId().length; i++) {
                Integer lecturerId = committeeDTO.getMemberLecturerId()[i];
                String roleStr = committeeDTO.getMemberRole()[i];
                if (lecturerId == null || roleStr == null) {
                    continue;
                }

                Lecturer lecturer = lecturerMap.get(lecturerId);
                if (lecturer == null) {
                    throw new EntityNotFoundException("Không tìm thấy giảng viên với ID: " + lecturerId);
                }

                CommitteeMember member = new CommitteeMember();
                // Sử dụng ID của committee đã lưu
                member.setCommitteeMemberPK(new CommitteeMemberPK(committee.getId(), lecturerId));
                member.setRole(parseRole(roleStr).name());
                member.setLecturer(lecturer);
                member.setCommittee(committee);
                committee.getCommitteeMembers().add(member);
            }
        }

        // Thêm luận văn
        if (committeeDTO.getThesesIds() != null) {
            for (Integer thesisId : committeeDTO.getThesesIds()) {
                if (thesisId == null) {
                    continue;
                }
                Thesis thesis = thesesMap.get(thesisId);
                if (thesis != null) {
                    thesis.setCommitteeId(committee);
                    committee.getTheses().add(thesis);
                } else {
                    System.err.println("Cảnh báo: Không tìm thấy luận văn với ID " + thesisId);
                }
            }
        }

        Committee savedCommittee = committeeRepository.addOrUpdate(committee);
        return this.toCommitteeDTO(savedCommittee);
    }

    @Override
    public void delete(int id) {
        committeeRepository.delete(id);
    }

    private CommitteeDTO toCommitteeDTO(Committee committee) {
        CommitteeDTO dto = new CommitteeDTO();

        dto.setId(committee.getId());
        dto.setDefenseDate(committee.getDefenseDate());
        dto.setLocation(committee.getLocation());
        dto.setStatus(committee.getStatus());
        dto.setIsActive(committee.isIsActive());

        // Map createdBy
        if (committee.getCreatedBy() != null) {
            dto.setCreatedByName(committee.getCreatedBy().getFirstName() + " " + committee.getCreatedBy().getLastName());
        }

        // Map committee members
        if (committee.getCommitteeMembers() != null) {
            List<CommitteeMemberDTO> memberDTOs = committee.getCommitteeMembers().stream()
                    .map(member -> {
                        CommitteeMemberDTO memberDTO = new CommitteeMemberDTO();
                        memberDTO.setLecturerId(member.getLecturer().getId());
                        memberDTO.setLecturerName(member.getLecturer().getFirstName() + " " + member.getLecturer().getLastName());
                        memberDTO.setRole(member.getRole());
                        return memberDTO;
                    })
                    .collect(Collectors.toList());
            dto.setCommitteeMembers(memberDTOs);
        }

        // Map thesis IDs
        if (committee.getTheses() != null) {
            List<Integer> thesisIds = committee.getTheses().stream()
                    .map(Thesis::getId)
                    .collect(Collectors.toList());
            dto.setThesesIds(thesisIds.toArray(new Integer[0]));
        }

        return dto;
    }

    private CommitteeListDTO toCommitteeListDTO(Committee committee) {
        CommitteeListDTO dto = modelMapper.map(committee, CommitteeListDTO.class);
        if (committee.getCreatedBy() != null && committee.getCreatedBy() != null) {
            dto.setCreatedByName(committee.getCreatedBy().getFirstName() + " " + committee.getCreatedBy().getLastName());
        }
        return dto;
    }
}
