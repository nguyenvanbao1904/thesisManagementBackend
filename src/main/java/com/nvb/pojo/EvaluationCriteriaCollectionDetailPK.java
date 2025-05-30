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
public class EvaluationCriteriaCollectionDetailPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "collection_id")
    private int collectionId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "criteria_id")
    private int criteriaId;

    public EvaluationCriteriaCollectionDetailPK() {
    }

    public EvaluationCriteriaCollectionDetailPK(int collectionId, int criteriaId) {
        this.collectionId = collectionId;
        this.criteriaId = criteriaId;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(int criteriaId) {
        this.criteriaId = criteriaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) collectionId;
        hash += (int) criteriaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EvaluationCriteriaCollectionDetailPK)) {
            return false;
        }
        EvaluationCriteriaCollectionDetailPK other = (EvaluationCriteriaCollectionDetailPK) object;
        if (this.collectionId != other.collectionId) {
            return false;
        }
        if (this.criteriaId != other.criteriaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.pojo.EvaluationCriteriaCollectionDetailPK[ collectionId=" + collectionId + ", criteriaId=" + criteriaId + " ]";
    }
    
}
