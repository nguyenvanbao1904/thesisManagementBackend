/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 *
 * @author nguyenvanbao
 */
public class EvaluationScoreDTO {
    @NotNull(message = "{evaluationScore.thesisId.notNullMsg}")
    private Integer thesisId;
    
    @NotNull(message = "{evaluationScore.criteriaId.notNullMsg}")
    private Integer criteriaId;
    
    @NotNull(message = "{evaluationScore.lecturerId.notNullMsg}")
    private Integer lecturerId;
    
    @NotNull(message = "{evaluationScore.score.notNullMsg}")
    @Min(value = 0, message = "{evaluationScore.score.minMsg}")
    @Max(value = 10, message = "{evaluationScore.score.maxMsg}")
    private Float score;
    
    private String comment;
    
    // Thông tin bổ sung cho hiển thị - có thể được lấy từ API khác
    private String criteriaName;
    private String lecturerName;
    private Float weight;
    private String thesisTitle;

    public EvaluationScoreDTO() {
    }

    public EvaluationScoreDTO(Integer thesisId, Integer criteriaId, Integer lecturerId, Float score, String comment, String criteriaName, String lecturerName, Float weight, String thesisTitle) {
        this.thesisId = thesisId;
        this.criteriaId = criteriaId;
        this.lecturerId = lecturerId;
        this.score = score;
        this.comment = comment;
        this.criteriaName = criteriaName;
        this.lecturerName = lecturerName;
        this.weight = weight;
        this.thesisTitle = thesisTitle;
    }

    public Integer getThesisId() {
        return thesisId;
    }

    public void setThesisId(Integer thesisId) {
        this.thesisId = thesisId;
    }

    public Integer getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Integer criteriaId) {
        this.criteriaId = criteriaId;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    @Override
    public String toString() {
        return "EvaluationScoreDTO{" +
                "thesisId=" + thesisId +
                ", criteriaId=" + criteriaId +
                ", lecturerId=" + lecturerId +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                '}';
    }
} 