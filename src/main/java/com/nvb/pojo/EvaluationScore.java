/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "evaluation_score")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluationScore.findAll", query = "SELECT e FROM EvaluationScore e"),
    @NamedQuery(name = "EvaluationScore.findByThesisId", query = "SELECT e FROM EvaluationScore e WHERE e.evaluationScorePK.thesisId = :thesisId"),
    @NamedQuery(name = "EvaluationScore.findByCriteriaId", query = "SELECT e FROM EvaluationScore e WHERE e.evaluationScorePK.criteriaId = :criteriaId"),
    @NamedQuery(name = "EvaluationScore.findByLecturerId", query = "SELECT e FROM EvaluationScore e WHERE e.evaluationScorePK.lecturerId = :lecturerId"),
    @NamedQuery(name = "EvaluationScore.findByScore", query = "SELECT e FROM EvaluationScore e WHERE e.score = :score")})
public class EvaluationScore implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluationScorePK evaluationScorePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private float score;
    @Lob
    @Size(max = 65535)
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "criteria_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EvaluationCriteria evaluationCriteria;
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Lecturer lecturer;
    @JoinColumn(name = "thesis_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Thesis thesis;

    public EvaluationScore() {
    }

    public EvaluationScore(EvaluationScorePK evaluationScorePK) {
        this.evaluationScorePK = evaluationScorePK;
    }

    public EvaluationScore(EvaluationScorePK evaluationScorePK, float score) {
        this.evaluationScorePK = evaluationScorePK;
        this.score = score;
    }

    public EvaluationScore(int thesisId, int criteriaId, int lecturerId) {
        this.evaluationScorePK = new EvaluationScorePK(thesisId, criteriaId, lecturerId);
    }

    public EvaluationScorePK getEvaluationScorePK() {
        return evaluationScorePK;
    }

    public void setEvaluationScorePK(EvaluationScorePK evaluationScorePK) {
        this.evaluationScorePK = evaluationScorePK;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EvaluationCriteria getEvaluationCriteria() {
        return evaluationCriteria;
    }

    public void setEvaluationCriteria(EvaluationCriteria evaluationCriteria) {
        this.evaluationCriteria = evaluationCriteria;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluationScorePK != null ? evaluationScorePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluationScore)) {
            return false;
        }
        EvaluationScore other = (EvaluationScore) object;
        if ((this.evaluationScorePK == null && other.evaluationScorePK != null) || (this.evaluationScorePK != null && !this.evaluationScorePK.equals(other.evaluationScorePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.EvaluationScore[ evaluationScorePK=" + evaluationScorePK + " ]";
    }
    
}
