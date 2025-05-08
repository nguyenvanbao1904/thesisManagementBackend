/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "committee_member")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommitteeMember.findAll", query = "SELECT c FROM CommitteeMember c"),
    @NamedQuery(name = "CommitteeMember.findByCommitteeId", query = "SELECT c FROM CommitteeMember c WHERE c.committeeMemberPK.committeeId = :committeeId"),
    @NamedQuery(name = "CommitteeMember.findByLecturerId", query = "SELECT c FROM CommitteeMember c WHERE c.committeeMemberPK.lecturerId = :lecturerId"),
    @NamedQuery(name = "CommitteeMember.findByRole", query = "SELECT c FROM CommitteeMember c WHERE c.role = :role")})
public class CommitteeMember implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CommitteeMemberPK committeeMemberPK;
    @Size(max = 14)
    @Column(name = "role")
    private String role;
    @JoinColumn(name = "committee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Committee committee;
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Lecturer lecturer;

    public CommitteeMember() {
    }

    public CommitteeMember(CommitteeMemberPK committeeMemberPK) {
        this.committeeMemberPK = committeeMemberPK;
    }

    public CommitteeMember(int committeeId, int lecturerId) {
        this.committeeMemberPK = new CommitteeMemberPK(committeeId, lecturerId);
    }

    public CommitteeMemberPK getCommitteeMemberPK() {
        return committeeMemberPK;
    }

    public void setCommitteeMemberPK(CommitteeMemberPK committeeMemberPK) {
        this.committeeMemberPK = committeeMemberPK;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Committee getCommittee() {
        return committee;
    }

    public void setCommittee(Committee committee) {
        this.committee = committee;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (committeeMemberPK != null ? committeeMemberPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommitteeMember)) {
            return false;
        }
        CommitteeMember other = (CommitteeMember) object;
        if ((this.committeeMemberPK == null && other.committeeMemberPK != null) || (this.committeeMemberPK != null && !this.committeeMemberPK.equals(other.committeeMemberPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.CommitteeMember[ committeeMemberPK=" + committeeMemberPK + " ]";
    }
    
}
