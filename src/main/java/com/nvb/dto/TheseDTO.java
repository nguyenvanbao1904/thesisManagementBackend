/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Committee;
import com.nvb.pojo.EvaluationFinalScore;
import com.nvb.pojo.EvaluationScore;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Student;
import java.util.Set;

/**
 *
 * @author nguyenvanbao
 */
public class TheseDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Set<Lecturer> lecturers;    
    private Set<Student> students;
    private Set<EvaluationScore> evaluationScores;
    private EvaluationFinalScore evaluationFinalScore;
    private AcademicStaff createdBy;
    private Committee committeeId;
    private EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionId;
    private Lecturer reviewerId;

    public TheseDTO(Integer id, String title, String description, String status, Set<Lecturer> lecturers, Set<Student> students, Set<EvaluationScore> evaluationScores, EvaluationFinalScore evaluationFinalScore, AcademicStaff createdBy, Committee committeeId, EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionId, Lecturer reviewerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.lecturers = lecturers;
        this.students = students;
        this.evaluationScores = evaluationScores;
        this.evaluationFinalScore = evaluationFinalScore;
        this.createdBy = createdBy;
        this.committeeId = committeeId;
        this.evaluationCriteriaCollectionId = evaluationCriteriaCollectionId;
        this.reviewerId = reviewerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Set<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<EvaluationScore> getEvaluationScores() {
        return evaluationScores;
    }

    public void setEvaluationScores(Set<EvaluationScore> evaluationScores) {
        this.evaluationScores = evaluationScores;
    }

    public EvaluationFinalScore getEvaluationFinalScore() {
        return evaluationFinalScore;
    }

    public void setEvaluationFinalScore(EvaluationFinalScore evaluationFinalScore) {
        this.evaluationFinalScore = evaluationFinalScore;
    }

    public AcademicStaff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AcademicStaff createdBy) {
        this.createdBy = createdBy;
    }

    public Committee getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(Committee committeeId) {
        this.committeeId = committeeId;
    }

    public EvaluationCriteriaCollectionDTO getEvaluationCriteriaCollectionId() {
        return evaluationCriteriaCollectionId;
    }

    public void setEvaluationCriteriaCollectionId(EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionId) {
        this.evaluationCriteriaCollectionId = evaluationCriteriaCollectionId;
    }

    public Lecturer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Lecturer reviewerId) {
        this.reviewerId = reviewerId;
    }
    
}
