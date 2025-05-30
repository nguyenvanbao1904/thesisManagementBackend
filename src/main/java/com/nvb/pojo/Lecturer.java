/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "lecturer")
@XmlRootElement
@DiscriminatorValue("ROLE_LECTURER")
@NamedQueries({
    @NamedQuery(name = "Lecturer.findAll", query = "SELECT l FROM Lecturer l"),
    @NamedQuery(name = "Lecturer.findById", query = "SELECT l FROM Lecturer l WHERE l.id = :id"),
    @NamedQuery(name = "Lecturer.findByAcademicTitle", query = "SELECT l FROM Lecturer l WHERE l.academicTitle = :academicTitle"),
    @NamedQuery(name = "Lecturer.findByAcademicDegree", query = "SELECT l FROM Lecturer l WHERE l.academicDegree = :academicDegree")})
@PrimaryKeyJoinColumn(name = "id") 
public class Lecturer extends User implements Serializable { 

    private static final long serialVersionUID = 1L;
    
    @Size(max = 19)
    @Column(name = "academic_title", nullable = true)
    private String academicTitle;

    @Size(max = 6)
    @Column(name = "academic_degree", nullable = false)
    @NotNull
    private String academicDegree;

    @ManyToMany(mappedBy = "lecturers")
    private Set<Thesis> thesesSupervisors;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecturer")
    private Set<CommitteeMember> committeeMembers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecturer")
    private Set<EvaluationScore> evaluationScores;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewerId")
    private Set<Thesis> thesesReviewer;

    public Lecturer() {
    }

    public Lecturer(Integer id) {
        super(id);
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

    public Set<Thesis> getThesesSupervisors() {
        return thesesSupervisors;
    }

    public void setThesesSupervisors(Set<Thesis> thesesSupervisors) {
        this.thesesSupervisors = thesesSupervisors;
    }

    public Set<CommitteeMember> getCommitteeMembers() {
        return committeeMembers;
    }

    public void setCommitteeMembers(Set<CommitteeMember> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    public Set<EvaluationScore> getEvaluationScores() {
        return evaluationScores;
    }

    public void setEvaluationScores(Set<EvaluationScore> evaluationScores) {
        this.evaluationScores = evaluationScores;
    }

    public Set<Thesis> getThesesReviewer() {
        return thesesReviewer;
    }

    public void setThesesReviewer(Set<Thesis> thesesReviewer) {
        this.thesesReviewer = thesesReviewer;
    }
}
