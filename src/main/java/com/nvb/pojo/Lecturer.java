/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import com.nvb.pojo.Thesis;
import com.nvb.pojo.User;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "lecturer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lecturer.findAll", query = "SELECT l FROM Lecturer l"),
    @NamedQuery(name = "Lecturer.findById", query = "SELECT l FROM Lecturer l WHERE l.id = :id"),
    @NamedQuery(name = "Lecturer.findByAcademicTitle", query = "SELECT l FROM Lecturer l WHERE l.academicTitle = :academicTitle"),
    @NamedQuery(name = "Lecturer.findByAcademicDegree", query = "SELECT l FROM Lecturer l WHERE l.academicDegree = :academicDegree")})
public class Lecturer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 19)
    @Column(name = "academic_title", nullable = true)
    private String academicTitle;
    @Size(max = 6)
    @Column(name = "academic_degree", nullable = false)
    @NotNull
    private String academicDegree;
    @JoinTable(name = "thesis_supervisors", joinColumns = {
        @JoinColumn(name = "lecturer_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "thesis_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<Thesis> thesesSupervisors;
    @JoinColumn(name = "id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecturer")
    private Set<CommitteeMember> committeeMembers;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecturer")
    private Set<EvaluationScore> evaluationScores;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewerId")
    private Set<Thesis> thesesReviewer;

    public Lecturer() {
    }

    public Lecturer(Integer id) {
        this.id = id;
    }

    public Lecturer(Integer id, String academicTitle) {
        this.id = id;
        this.academicTitle = academicTitle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @XmlTransient
    public Set<Thesis> getThesesSupervisors() {
        return thesesSupervisors;
    }

    public void setThesesSupervisors(Set<Thesis> thesesSupervisors) {
        this.thesesSupervisors = thesesSupervisors;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public Set<CommitteeMember> getcommitteeMembers() {
        return committeeMembers;
    }

    public void setcommitteeMembers(Set<CommitteeMember> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    @XmlTransient
    public Set<EvaluationScore> getevaluationScores() {
        return evaluationScores;
    }

    public void setevaluationScores(Set<EvaluationScore> evaluationScores) {
        this.evaluationScores = evaluationScores;
    }

    @XmlTransient
    public Set<Thesis> getThesesReviewer() {
        return thesesReviewer;
    }

    public void setThesesReviewer(Set<Thesis> thesesReviewer) {
        this.thesesReviewer = thesesReviewer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lecturer)) {
            return false;
        }
        Lecturer other = (Lecturer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.Lecturer[ id=" + id + " ]";
    }
    
}
