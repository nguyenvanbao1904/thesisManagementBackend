/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "academic_staff")
@XmlRootElement
@DiscriminatorValue("ROLE_ACADEMICSTAFF")
@NamedQueries({
    @NamedQuery(name = "AcademicStaff.findAll", query = "SELECT a FROM AcademicStaff a"),
    @NamedQuery(name = "AcademicStaff.findById", query = "SELECT a FROM AcademicStaff a WHERE a.id = :id")})
@PrimaryKeyJoinColumn(name = "id")
public class AcademicStaff extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Set<Committee> committees;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Set<EvaluationCriteriaCollection> evaluationCriteriaCollections;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy")
    private Set<Thesis> theses;

    public AcademicStaff() {
    }

    public AcademicStaff(Integer id) {
        super(id);
    }

    public Set<Committee> getCommittees() {
        return committees;
    }

    public void setCommittees(Set<Committee> committees) {
        this.committees = committees;
    }

    public Set<EvaluationCriteriaCollection> getEvaluationCriteriaCollections() {
        return evaluationCriteriaCollections;
    }

    public void setEvaluationCriteriaCollections(Set<EvaluationCriteriaCollection> evaluationCriteriaCollections) {
        this.evaluationCriteriaCollections = evaluationCriteriaCollections;
    }

    public Set<Thesis> getTheses() {
        return theses;
    }

    public void setTheses(Set<Thesis> theses) {
        this.theses = theses;
    }
}
