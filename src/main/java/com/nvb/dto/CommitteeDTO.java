/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author nguyenvanbao
 */
public class CommitteeDTO {

    private Integer id;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    @Future(message = "{committee.defenseDate.future}")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime defenseDate;
    
    @NotBlank(message = "{committee.location.notBlank}")
    private String location;
    
    private String status;
    private Boolean isActive;
    
    // Thông tin thành viên hội đồng
    @NotNull(message = "{committee.memberLecturerId.notNull}")
    private Integer[] memberLecturerId;
    
    @NotNull(message = "{committee.memberRole.notNull}")
    private String[] memberRole;
    
    // Danh sách khóa luận thuộc hội đồng
    private Integer[] thesesIds;
    
    // Thông tin bổ sung cho hiển thị
    private List<CommitteeMemberDTO> committeeMembers;
    private List<ThesesDTO> theses;
    private String createdByName;

    public CommitteeDTO() {
    }

    public CommitteeDTO(Integer id, LocalDateTime defenseDate, String location, String status, 
                      Boolean isActive, Integer[] memberLecturerId, String[] memberRole, 
                      Integer[] thesesIds, String createdByName) {
        this.id = id;
        this.defenseDate = defenseDate;
        this.location = location;
        this.status = status;
        this.isActive = isActive;
        this.memberLecturerId = memberLecturerId;
        this.memberRole = memberRole;
        this.thesesIds = thesesIds;
        this.createdByName = createdByName;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public List<CommitteeMemberDTO> getCommitteeMembers() {
        return committeeMembers;
    }

    public void setCommitteeMembers(List<CommitteeMemberDTO> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    public List<ThesesDTO> getTheses() {
        return theses;
    }

    public void setTheses(List<ThesesDTO> theses) {
        this.theses = theses;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Override
    public String toString() {
        return "CommitteeDTO{" +
                "id=" + id +
                ", defenseDate=" + defenseDate +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", memberLecturerId=" + (memberLecturerId != null ? memberLecturerId.length : "null") +
                ", memberRole=" + (memberRole != null ? memberRole.length : "null") +
                ", thesesIds=" + (thesesIds != null ? thesesIds.length : "null") +
                '}';
    }
}
