/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author nguyenvanbao
 */
@Embeddable
public class CommitteeMemberPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "committee_id")
    private int committeeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lecturer_id")
    private int lecturerId;

    public CommitteeMemberPK() {
    }

    public CommitteeMemberPK(int committeeId, int lecturerId) {
        this.committeeId = committeeId;
        this.lecturerId = lecturerId;
    }

    public int getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(int committeeId) {
        this.committeeId = committeeId;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) committeeId;
        hash += (int) lecturerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommitteeMemberPK)) {
            return false;
        }
        CommitteeMemberPK other = (CommitteeMemberPK) object;
        if (this.committeeId != other.committeeId) {
            return false;
        }
        if (this.lecturerId != other.lecturerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.CommitteeMemberPK[ committeeId=" + committeeId + ", lecturerId=" + lecturerId + " ]";
    }
    
}
