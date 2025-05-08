/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "academic_staff")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AcademicStaff.findAll", query = "SELECT a FROM AcademicStaff a"),
    @NamedQuery(name = "AcademicStaff.findById", query = "SELECT a FROM AcademicStaff a WHERE a.id = :id")})
public class AcademicStaff implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Set<Committee> committees;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Set<EvaluationCriteriaCollection> evaluationCriteriaCollections;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Set<Thesis> theses;

    public AcademicStaff() {
    }

    public AcademicStaff(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public Set<Committee> getCommittees() {
        return committees;
    }

    public void setCommittees(Set<Committee> committees) {
        this.committees = committees;
    }

    @XmlTransient
    public Set<EvaluationCriteriaCollection> getEvaluationCriteriaCollections() {
        return evaluationCriteriaCollections;
    }

    public void setEvaluationCriteriaCollections(Set<EvaluationCriteriaCollection> evaluationCriteriaCollections) {
        this.evaluationCriteriaCollections = evaluationCriteriaCollections;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public Set<Thesis> getTheses() {
        return theses;
    }

    public void setTheses(Set<Thesis> theses) {
        this.theses = theses;
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
        if (!(object instanceof AcademicStaff)) {
            return false;
        }
        AcademicStaff other = (AcademicStaff) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.AcademicStaff[ id=" + id + " ]";
    }
    
}
