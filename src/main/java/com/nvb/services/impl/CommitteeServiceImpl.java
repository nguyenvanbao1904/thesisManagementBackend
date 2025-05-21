/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.CommitteeDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Committee;
import com.nvb.pojo.CommitteeMember;
import com.nvb.pojo.CommitteeMemberPK;
import com.nvb.pojo.CommitteeMemberRole;
import com.nvb.pojo.CommitteeStatus;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Thesis;
import com.nvb.repositories.CommitteeRepository;
import com.nvb.services.AcademicsStaffService;
import com.nvb.services.CommitteeService;
import com.nvb.services.LecturerService;
import com.nvb.services.ThesesService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private AcademicsStaffService academicsStaffService;

    @Autowired
    private ThesesService thesesService;

    @Autowired
    private LecturerService lecturerService;

    @Override
    public List<Committee> getCommittees(Map<String, String> params) {
        return getCommittees(params, false);
    }

    @Override
    public List<Committee> getCommittees(Map<String, String> params, boolean pagination) {
        return getCommittees(params, pagination, false);
    }

    @Override
    public Committee addOrUpdate(CommitteeDTO committeeDTO) {
        Committee committee;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff academicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffService.getAcademicStaff(Map.of("username", authentication.getName()));
        }

        if (committeeDTO.getId() == null) {
            committee = new Committee();
            committee.setStatus(CommitteeStatus.LOCKED.toString());
            committee.setCommitteeMembers(new HashSet<>());
            committee.setTheses(new HashSet<>());
        } else {
            committee = committeeRepository.getCommittee(new HashMap<>(Map.of("id", String.valueOf(committeeDTO.getId()))));

            // Xóa các thành viên cũ khỏi collection
            committee.getCommitteeMembers().clear();

            // Xóa các thesis khỏi collection và cập nhật tham chiếu
            if (committee.getTheses() != null) {
                committee.getTheses().forEach(thesis -> thesis.setCommitteeId(null));
                committee.getTheses().clear();
            }
        }

        committee.setCreatedBy(academicStaff);
        committee.setDefenseDate(committeeDTO.getDefenseDate());
        committee.setIsActive(true);
        committee.setLocation(committeeDTO.getLocation());

        // Lưu trước để lấy ID
        committee = committeeRepository.addOrUpdate(committee);

        // Tạo các thành viên mới
        if (committeeDTO.getMemberLecturerId() != null && committeeDTO.getMemberRole() != null) {
            for (int i = 0; i < committeeDTO.getMemberLecturerId().length; i++) {
                // Chỉ thêm các giảng viên được chọn
                if (committeeDTO.getMemberLecturerId()[i] != null) {
                    CommitteeMember member = new CommitteeMember();

                    String roleStr = committeeDTO.getMemberRole()[i];
                    CommitteeMemberRole role;

                    try {
                        switch (roleStr) {
                            case "CHAIRMAN":
                                role = CommitteeMemberRole.ROLE_CHAIRMAN;
                                break;
                            case "SECRETARY":
                                role = CommitteeMemberRole.ROLE_SECRETARY;
                                break;
                            case "REVIEWER":
                                role = CommitteeMemberRole.ROLE_REVIEWER;
                                break;
                            default:
                                role = CommitteeMemberRole.ROLE_MEMBER;
                                break;
                        }

                        member.setRole(role.name());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid committee role: " + roleStr);
                    }

                    // Tạo khóa chính tổng hợp
                    CommitteeMemberPK pk = new CommitteeMemberPK();
                    pk.setCommitteeId(committee.getId());
                    pk.setLecturerId(committeeDTO.getMemberLecturerId()[i]);
                    member.setCommitteeMemberPK(pk);

                    // Lấy đối tượng Lecturer
                    Lecturer lecturer = lecturerService.getLecturerWithDetails(Map.of("id", committeeDTO.getMemberLecturerId()[i].toString()));
                    member.setLecturer(lecturer);

                    // Thiết lập mối quan hệ
                    member.setCommittee(committee);

                    committee.getCommitteeMembers().add(member);
                }
            }
        }

        // Tạo Set cho theses
        if (committeeDTO.getThesesIds() != null && committeeDTO.getThesesIds().length > 0) {
            for (Integer thesisId : committeeDTO.getThesesIds()) {
                Thesis thesis = thesesService.getThesis(Map.of("id", thesisId.toString()));
                if (thesis != null) {
                    thesis.setCommitteeId(committee);
                    committee.getTheses().add(thesis);
                }
            }
        }

        // Cập nhật lại committee sau khi đã thiết lập các mối quan hệ
        return committeeRepository.addOrUpdate(committee);
    }

    @Override
    public Committee getCommittee(Map<String, String> params) {
        return committeeRepository.getCommittee(params);
    }

    @Override
    public List<Committee> getCommittees(Map<String, String> params, boolean pagination, boolean details) {
        return committeeRepository.getCommittees(params, pagination, details);
    }

    @Override
    public void deleteCommittee(int id) {
        committeeRepository.deleteCommittee(id);
    }
}
