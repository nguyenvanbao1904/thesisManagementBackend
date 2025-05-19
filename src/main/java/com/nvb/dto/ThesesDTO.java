/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Committee;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.EvaluationFinalScore;
import com.nvb.pojo.EvaluationScore;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nguyenvanbao
 */
public class ThesesDTO {

    private Integer id;
    @NotBlank(message = "{theses.title.notnullMsg}")
    private String title;
    @NotBlank(message = "{theses.description.notnullMsg}")
    private String description;
    private String status;
    @NotNull(message = "{theses.lecturers.notnullMsg}")
    private Set<Lecturer> lecturers;
    @NotNull(message = "{theses.students.notnullMsg}")
    private Set<Student> students;
    private Set<EvaluationScore> evaluationScores;
    private EvaluationFinalScore evaluationFinalScore;
    private AcademicStaff createdBy;
    private Committee committeeId;
    @NotNull(message = "{theses.evaluationCriteriaCollection.notnullMsg}")
    private EvaluationCriteriaCollection evaluationCriteriaCollectionId;
    @NotNull(message = "{theses.reviewerId.notnullMsg}")
    private Lecturer reviewerId;
    private MultipartFile file;

    public ThesesDTO(Integer id, String title, String description, Set<Lecturer> lecturers, Set<Student> students, Committee committeeId, EvaluationCriteriaCollection evaluationCriteriaCollectionId, Lecturer reviewerId, MultipartFile file) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lecturers = lecturers;
        this.students = students;
        this.committeeId = committeeId;
        this.evaluationCriteriaCollectionId = evaluationCriteriaCollectionId;
        this.reviewerId = reviewerId;
        this.file = file;
    }

    public ThesesDTO() {
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ThesesDTO{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", description='" + description + '\''
                + ", status='" + status + '\''
                + ", lecturers=" + (lecturers != null ? lecturers.size() : "null")
                + ", students=" + (students != null ? students.size() : "null")
                + ", committeeId=" + (committeeId != null ? committeeId.getId() : "null")
                + ", evaluationCriteriaCollectionId=" + (evaluationCriteriaCollectionId != null ? evaluationCriteriaCollectionId.getId() : "null")
                + ", reviewerId=" + (reviewerId != null ? reviewerId.getId() : "null")
                + ", file=" + (file != null ? file.getOriginalFilename() : "null")
                + '}';
    }
}
