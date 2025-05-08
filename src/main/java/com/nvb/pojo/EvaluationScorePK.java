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
public class EvaluationScorePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "thesis_id")
    private int thesisId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "criteria_id")
    private int criteriaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lecturer_id")
    private int lecturerId;

    public EvaluationScorePK() {
    }

    public EvaluationScorePK(int thesisId, int criteriaId, int lecturerId) {
        this.thesisId = thesisId;
        this.criteriaId = criteriaId;
        this.lecturerId = lecturerId;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public int getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(int criteriaId) {
        this.criteriaId = criteriaId;
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
        hash += (int) thesisId;
        hash += (int) criteriaId;
        hash += (int) lecturerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluationScorePK)) {
            return false;
        }
        EvaluationScorePK other = (EvaluationScorePK) object;
        if (this.thesisId != other.thesisId) {
            return false;
        }
        if (this.criteriaId != other.criteriaId) {
            return false;
        }
        if (this.lecturerId != other.lecturerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.EvaluationScorePK[ thesisId=" + thesisId + ", criteriaId=" + criteriaId + ", lecturerId=" + lecturerId + " ]";
    }
    
}
