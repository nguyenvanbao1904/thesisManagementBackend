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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Table(name = "evaluation_criteria_collection")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluationCriteriaCollection.findAll", query = "SELECT e FROM EvaluationCriteriaCollection e"),
    @NamedQuery(name = "EvaluationCriteriaCollection.findById", query = "SELECT e FROM EvaluationCriteriaCollection e WHERE e.id = :id"),
    @NamedQuery(name = "EvaluationCriteriaCollection.findByName", query = "SELECT e FROM EvaluationCriteriaCollection e WHERE e.name = :name")})
public class EvaluationCriteriaCollection implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluationCriteriaCollection")
    private Set<EvaluationCriteriaCollectionDetail> evaluationCriteriaCollectionDetails;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AcademicStaff createdBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluationCriteriaCollectionId")
    private Set<Thesis> theses;

    public EvaluationCriteriaCollection() {
    }

    public EvaluationCriteriaCollection(Integer id) {
        this.id = id;
    }

    public EvaluationCriteriaCollection(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @XmlTransient
    public Set<EvaluationCriteriaCollectionDetail> getevaluationCriteriaCollectionDetails() {
        return evaluationCriteriaCollectionDetails;
    }

    public void setevaluationCriteriaCollectionDetails(Set<EvaluationCriteriaCollectionDetail> evaluationCriteriaCollectionDetails) {
        this.evaluationCriteriaCollectionDetails = evaluationCriteriaCollectionDetails;
    }

    public AcademicStaff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AcademicStaff createdBy) {
        this.createdBy = createdBy;
    }

    @XmlTransient
    public Set<Thesis> gettheses() {
        return theses;
    }

    public void settheses(Set<Thesis> theses) {
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
        if (!(object instanceof EvaluationCriteriaCollection)) {
            return false;
        }
        EvaluationCriteriaCollection other = (EvaluationCriteriaCollection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.EvaluationCriteriaCollection[ id=" + id + " ]";
    }
    
}
