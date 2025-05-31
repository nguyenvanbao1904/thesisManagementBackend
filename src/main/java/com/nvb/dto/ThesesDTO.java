/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

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
    private Set<Integer> lecturerIds;
    
    @NotNull(message = "{theses.students.notnullMsg}")
    private Set<Integer> studentIds;
    
    private Integer committeeId;
    
    @NotNull(message = "{theses.evaluationCriteriaCollection.notnullMsg}")
    private Integer evaluationCriteriaCollectionId;
    
    @NotNull(message = "{theses.reviewerId.notnullMsg}")
    private Integer reviewerId;
    
    private MultipartFile file;
    
    private String fileUrl;
    
    // Thông tin bổ sung cho hiển thị
    private String committeeName;
    private String evaluationCriteriaCollectionName;
    private String reviewerName;
    private Float averageScore;
    private Set<UserDTO> lecturers;
    private Set<UserDTO> students;

    public ThesesDTO() {
    }

    public ThesesDTO(Integer id, String title, String description, String status, 
                    Set<Integer> lecturerIds, Set<Integer> studentIds, 
                    Integer committeeId, Integer evaluationCriteriaCollectionId, 
                    Integer reviewerId, MultipartFile file) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.lecturerIds = lecturerIds;
        this.studentIds = studentIds;
        this.committeeId = committeeId;
        this.evaluationCriteriaCollectionId = evaluationCriteriaCollectionId;
        this.reviewerId = reviewerId;
        this.file = file;
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

    public Set<Integer> getLecturerIds() {
        return lecturerIds;
    }

    public void setLecturerIds(Set<Integer> lecturerIds) {
        this.lecturerIds = lecturerIds;
    }

    public Set<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(Set<Integer> studentIds) {
        this.studentIds = studentIds;
    }

    public Integer getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(Integer committeeId) {
        this.committeeId = committeeId;
    }

    public Integer getEvaluationCriteriaCollectionId() {
        return evaluationCriteriaCollectionId;
    }

    public void setEvaluationCriteriaCollectionId(Integer evaluationCriteriaCollectionId) {
        this.evaluationCriteriaCollectionId = evaluationCriteriaCollectionId;
    }

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCommitteeName() {
        return committeeName;
    }

    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }

    public String getEvaluationCriteriaCollectionName() {
        return evaluationCriteriaCollectionName;
    }

    public void setEvaluationCriteriaCollectionName(String evaluationCriteriaCollectionName) {
        this.evaluationCriteriaCollectionName = evaluationCriteriaCollectionName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }

    public Set<UserDTO> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Set<UserDTO> lecturers) {
        this.lecturers = lecturers;
    }

    public Set<UserDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<UserDTO> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "ThesesDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", lecturerIds=" + (lecturerIds != null ? lecturerIds.size() : "null") +
                ", studentIds=" + (studentIds != null ? studentIds.size() : "null") +
                ", committeeId=" + committeeId +
                ", evaluationCriteriaCollectionId=" + evaluationCriteriaCollectionId +
                ", reviewerId=" + reviewerId +
                ", file=" + (file != null ? file.getOriginalFilename() : "null") +
                '}';
    }
}
