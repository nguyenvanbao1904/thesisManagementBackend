/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
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
@Table(name = "evaluation_criteria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluationCriteria.findAll", query = "SELECT e FROM EvaluationCriteria e"),
    @NamedQuery(name = "EvaluationCriteria.findById", query = "SELECT e FROM EvaluationCriteria e WHERE e.id = :id"),
    @NamedQuery(name = "EvaluationCriteria.findByName", query = "SELECT e FROM EvaluationCriteria e WHERE e.name = :name"),
    @NamedQuery(name = "EvaluationCriteria.findByMaxPoint", query = "SELECT e FROM EvaluationCriteria e WHERE e.maxPoint = :maxPoint")})
public class EvaluationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_point")
    private float maxPoint;
    @OneToMany(mappedBy = "evaluationCriteria")
    private Set<EvaluationCriteriaCollectionDetail> evaluationCriteriaCollectionDetails;
    @OneToMany(mappedBy = "evaluationCriteria")
    private Set<EvaluationScore> evaluationScores;

    public EvaluationCriteria() {
    }

    public EvaluationCriteria(Integer id) {
        this.id = id;
    }

    public EvaluationCriteria(Integer id, String name, String description, float maxPoint) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxPoint = maxPoint;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(float maxPoint) {
        this.maxPoint = maxPoint;
    }

    @XmlTransient
    public Set<EvaluationCriteriaCollectionDetail> getevaluationCriteriaCollectionDetails() {
        return evaluationCriteriaCollectionDetails;
    }

    public void setevaluationCriteriaCollectionDetails(Set<EvaluationCriteriaCollectionDetail> evaluationCriteriaCollectionDetails) {
        this.evaluationCriteriaCollectionDetails = evaluationCriteriaCollectionDetails;
    }

    @XmlTransient
    public Set<EvaluationScore> getevaluationScores() {
        return evaluationScores;
    }

    public void setevaluationScores(Set<EvaluationScore> evaluationScores) {
        this.evaluationScores = evaluationScores;
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
        if (!(object instanceof EvaluationCriteria)) {
            return false;
        }
        EvaluationCriteria other = (EvaluationCriteria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.EvaluationCriteria[ id=" + id + " ]";
    }
    
}
