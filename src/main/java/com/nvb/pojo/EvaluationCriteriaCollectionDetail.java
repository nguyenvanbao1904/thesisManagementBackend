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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "evaluation_criteria_collection_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluationCriteriaCollectionDetail.findAll", query = "SELECT e FROM EvaluationCriteriaCollectionDetail e"),
    @NamedQuery(name = "EvaluationCriteriaCollectionDetail.findByCollectionId", query = "SELECT e FROM EvaluationCriteriaCollectionDetail e WHERE e.evaluationCriteriaCollectionDetailPK.collectionId = :collectionId"),
    @NamedQuery(name = "EvaluationCriteriaCollectionDetail.findByCriteriaId", query = "SELECT e FROM EvaluationCriteriaCollectionDetail e WHERE e.evaluationCriteriaCollectionDetailPK.criteriaId = :criteriaId"),
    @NamedQuery(name = "EvaluationCriteriaCollectionDetail.findByWeight", query = "SELECT e FROM EvaluationCriteriaCollectionDetail e WHERE e.weight = :weight")})
public class EvaluationCriteriaCollectionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluationCriteriaCollectionDetailPK evaluationCriteriaCollectionDetailPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weight")
    private float weight;
    @JoinColumn(name = "criteria_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EvaluationCriteria evaluationCriteria;
    @JoinColumn(name = "collection_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EvaluationCriteriaCollection evaluationCriteriaCollection;

    public EvaluationCriteriaCollectionDetail() {
    }

    public EvaluationCriteriaCollectionDetail(EvaluationCriteriaCollectionDetailPK evaluationCriteriaCollectionDetailPK) {
        this.evaluationCriteriaCollectionDetailPK = evaluationCriteriaCollectionDetailPK;
    }

    public EvaluationCriteriaCollectionDetail(EvaluationCriteriaCollectionDetailPK evaluationCriteriaCollectionDetailPK, float weight) {
        this.evaluationCriteriaCollectionDetailPK = evaluationCriteriaCollectionDetailPK;
        this.weight = weight;
    }

    public EvaluationCriteriaCollectionDetail(int collectionId, int criteriaId) {
        this.evaluationCriteriaCollectionDetailPK = new EvaluationCriteriaCollectionDetailPK(collectionId, criteriaId);
    }

    public EvaluationCriteriaCollectionDetailPK getEvaluationCriteriaCollectionDetailPK() {
        return evaluationCriteriaCollectionDetailPK;
    }

    public void setEvaluationCriteriaCollectionDetailPK(EvaluationCriteriaCollectionDetailPK evaluationCriteriaCollectionDetailPK) {
        this.evaluationCriteriaCollectionDetailPK = evaluationCriteriaCollectionDetailPK;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public EvaluationCriteria getEvaluationCriteria() {
        return evaluationCriteria;
    }

    public void setEvaluationCriteria(EvaluationCriteria evaluationCriteria) {
        this.evaluationCriteria = evaluationCriteria;
    }

    public EvaluationCriteriaCollection getEvaluationCriteriaCollection() {
        return evaluationCriteriaCollection;
    }

    public void setEvaluationCriteriaCollection(EvaluationCriteriaCollection evaluationCriteriaCollection) {
        this.evaluationCriteriaCollection = evaluationCriteriaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluationCriteriaCollectionDetailPK != null ? evaluationCriteriaCollectionDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluationCriteriaCollectionDetail)) {
            return false;
        }
        EvaluationCriteriaCollectionDetail other = (EvaluationCriteriaCollectionDetail) object;
        if ((this.evaluationCriteriaCollectionDetailPK == null && other.evaluationCriteriaCollectionDetailPK != null) || (this.evaluationCriteriaCollectionDetailPK != null && !this.evaluationCriteriaCollectionDetailPK.equals(other.evaluationCriteriaCollectionDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.EvaluationCriteriaCollectionDetail[ evaluationCriteriaCollectionDetailPK=" + evaluationCriteriaCollectionDetailPK + " ]";
    }
    
}
