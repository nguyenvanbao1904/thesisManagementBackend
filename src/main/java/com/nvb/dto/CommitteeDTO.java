/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.CommitteeMember;
import com.nvb.pojo.Thesis;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author nguyenvanbao
 */
public class CommitteeDTO {

    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "{committee.defenseDate.future}")
    private LocalDateTime defenseDate;
    @NotBlank
    private String location;
    private String status;
    private AcademicStaff createdBy;
    private Set<CommitteeMember> committeeMembers;
    private Set<Thesis> theses;
    private Integer[] memberLecturerId;
    private String[] memberRole;
    private Integer[] thesesIds;

    public CommitteeDTO(Integer id, LocalDateTime defenseDate, String location, String status, AcademicStaff createdBy, Set<CommitteeMember> committeeMembers, Set<Thesis> theses) {
        this.id = id;
        this.defenseDate = defenseDate;
        this.location = location;
        this.status = status;
        this.createdBy = createdBy;
        this.committeeMembers = committeeMembers;
        this.theses = theses;
    }

    public CommitteeDTO(Integer id, LocalDateTime defenseDate, String location, String status, AcademicStaff createdBy, Set<CommitteeMember> committeeMembers, Set<Thesis> theses, Integer[] memberLecturerId, String[] memberRole, Integer[] thesesIds) {
        this.id = id;
        this.defenseDate = defenseDate;
        this.location = location;
        this.status = status;
        this.createdBy = createdBy;
        this.committeeMembers = committeeMembers;
        this.theses = theses;
        this.memberLecturerId = memberLecturerId;
        this.memberRole = memberRole;
        this.thesesIds = thesesIds;
    }
    
    public CommitteeDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDateTime defenseDate) {
        this.defenseDate = defenseDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AcademicStaff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AcademicStaff createdBy) {
        this.createdBy = createdBy;
    }

    public Set<CommitteeMember> getCommitteeMembers() {
        return committeeMembers;
    }

    public void setCommitteeMembers(Set<CommitteeMember> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    public Set<Thesis> getTheses() {
        return theses;
    }

    public void setTheses(Set<Thesis> theses) {
        this.theses = theses;
    }

    public Integer[] getMemberLecturerId() {
        return memberLecturerId;
    }

    public void setMemberLecturerId(Integer[] memberLecturerId) {
        this.memberLecturerId = memberLecturerId;
    }

    public String[] getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String[] memberRole) {
        this.memberRole = memberRole;
    }

    public Integer[] getThesesIds() {
        return thesesIds;
    }

    public void setThesesIds(Integer[] thesesIds) {
        this.thesesIds = thesesIds;
    }
}
