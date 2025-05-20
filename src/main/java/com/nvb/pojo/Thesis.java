/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "thesis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Thesis.findAll", query = "SELECT t FROM Thesis t"),
    @NamedQuery(name = "Thesis.findById", query = "SELECT t FROM Thesis t WHERE t.id = :id"),
    @NamedQuery(name = "Thesis.findByTitle", query = "SELECT t FROM Thesis t WHERE t.title = :title"),
    @NamedQuery(name = "Thesis.findByStatus", query = "SELECT t FROM Thesis t WHERE t.status = :status"),
    @NamedQuery(name = "Thesis.findByCreatedAt", query = "SELECT t FROM Thesis t WHERE t.createdAt = :createdAt"),
    @NamedQuery(name = "Thesis.findByUpdatedAt", query = "SELECT t FROM Thesis t WHERE t.updatedAt = :updatedAt")})
public class Thesis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;
    @ManyToMany
    @JoinTable(
            name = "thesis_supervisors",
            joinColumns = @JoinColumn(name = "thesis_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id")
    )
    private Set<Lecturer> lecturers;
    @ManyToMany
    @JoinTable(
            name = "thesis_student",
            joinColumns = @JoinColumn(name = "thesis_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "thesis", fetch = FetchType.LAZY)
    private Set<EvaluationScore> evaluationScores;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "thesis")
    private EvaluationFinalScore evaluationFinalScore;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private AcademicStaff createdBy;
    @JoinColumn(name = "committee_id", referencedColumnName = "id")
    @ManyToOne
    private Committee committeeId;
    @JoinColumn(name = "evaluation_criteria_collection_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EvaluationCriteriaCollection evaluationCriteriaCollectionId;
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Lecturer reviewerId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "file_url")
    private String fileUrl;

    public Thesis() {
    }

    public Thesis(Integer id) {
        this.id = id;
    }

    public Thesis(Integer id, String title, String description, String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public EvaluationCriteriaCollection getEvaluationCriteriaCollectionId() {
        return evaluationCriteriaCollectionId;
    }

    public void setEvaluationCriteriaCollectionId(EvaluationCriteriaCollection evaluationCriteriaCollectionId) {
        this.evaluationCriteriaCollectionId = evaluationCriteriaCollectionId;
    }

    public Lecturer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Lecturer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
        if (!(object instanceof Thesis)) {
            return false;
        }
        Thesis other = (Thesis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.Thesis[ id=" + id + " ]";
    }

}
