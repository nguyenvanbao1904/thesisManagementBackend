/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import java.util.Set;

/**
 *
 * @author nguyenvanbao
 */
public class LecturerAssignmentsDTO {
    private Set<ThesesDTO> supervisorTheses;
    private Set<ThesesDTO> reviewerTheses;   
    private Set<CommitteeMemberDTO> committeeMembers;

    public Set<ThesesDTO> getSupervisorTheses() {
        return supervisorTheses;
    }

    public void setSupervisorTheses(Set<ThesesDTO> supervisorTheses) {
        this.supervisorTheses = supervisorTheses;
    }

    public Set<ThesesDTO> getReviewerTheses() {
        return reviewerTheses;
    }

    public void setReviewerTheses(Set<ThesesDTO> reviewerTheses) {
        this.reviewerTheses = reviewerTheses;
    }

    public Set<CommitteeMemberDTO> getCommitteeMembers() {
        return committeeMembers;
    }

    public void setCommitteeMembers(Set<CommitteeMemberDTO> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }
}
