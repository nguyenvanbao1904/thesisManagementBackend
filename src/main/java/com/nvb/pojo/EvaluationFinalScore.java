/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
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
@Table(name = "evaluation_final_score")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluationFinalScore.findAll", query = "SELECT e FROM EvaluationFinalScore e"),
    @NamedQuery(name = "EvaluationFinalScore.findById", query = "SELECT e FROM EvaluationFinalScore e WHERE e.id = :id"),
    @NamedQuery(name = "EvaluationFinalScore.findByAverageScore", query = "SELECT e FROM EvaluationFinalScore e WHERE e.averageScore = :averageScore")})
public class EvaluationFinalScore implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "average_score")
    private float averageScore;
    @Lob
    @Size(max = 65535)
    @Column(name = "chairman_comment")
    private String chairmanComment;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Thesis thesis;

    public EvaluationFinalScore() {
    }

    public EvaluationFinalScore(Integer id) {
        this.id = id;
    }

    public EvaluationFinalScore(Integer id, float averageScore) {
        this.id = id;
        this.averageScore = averageScore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public String getChairmanComment() {
        return chairmanComment;
    }

    public void setChairmanComment(String chairmanComment) {
        this.chairmanComment = chairmanComment;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluationFinalScore)) {
            return false;
        }
        EvaluationFinalScore other = (EvaluationFinalScore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.EvaluationFinalScore[ id=" + id + " ]";
    }
    
}
