/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author nguyenvanbao
 */
public class CommitteeMemberDTO {
    @NotNull(message = "{committeeMember.committeeId.notNullMsg}")
    private Integer committeeId;
    
    @NotNull(message = "{committeeMember.lecturerId.notNullMsg}")
    private Integer lecturerId;
    
    @NotNull(message = "{committeeMember.role.notNullMsg}")
    @Size(max = 14, message = "{committeeMember.role.sizeMsg}")
    private String role;
    
    // Thông tin bổ sung cho hiển thị
    private String lecturerName;
    private String academicTitle;
    private String academicDegree;
    private String committeeName;

    public CommitteeMemberDTO() {
    }

    public CommitteeMemberDTO(Integer committeeId, Integer lecturerId, String role, String lecturerName, String academicTitle, String academicDegree, String committeeName) {
        this.committeeId = committeeId;
        this.lecturerId = lecturerId;
        this.role = role;
        this.lecturerName = lecturerName;
        this.academicTitle = academicTitle;
        this.academicDegree = academicDegree;
        this.committeeName = committeeName;
    }

    public Integer getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(Integer committeeId) {
        this.committeeId = committeeId;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public String getCommitteeName() {
        return committeeName;
    }

    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }

    @Override
    public String toString() {
        return "CommitteeMemberDTO{" +
                "committeeId=" + committeeId +
                ", lecturerId=" + lecturerId +
                ", role='" + role + '\'' +
                ", lecturerName='" + lecturerName + '\'' +
                '}';
    }
} 