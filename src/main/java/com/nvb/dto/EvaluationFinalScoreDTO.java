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
public class EvaluationFinalScoreDTO {
    private Integer id;
    
    @NotNull(message = "{evaluationFinalScore.thesisId.notNullMsg}")
    private Integer thesisId;
    
    @NotNull(message = "{evaluationFinalScore.averageScore.notNullMsg}")
    @Min(value = 0, message = "{evaluationFinalScore.averageScore.minMsg}")
    @Max(value = 10, message = "{evaluationFinalScore.averageScore.maxMsg}")
    private Float averageScore;
    
    private String chairmanComment;
    
    // Thông tin bổ sung cho hiển thị
    private String thesisTitle;

    public EvaluationFinalScoreDTO() {
    }

    public EvaluationFinalScoreDTO(Integer id, Integer thesisId, Float averageScore, String chairmanComment, String thesisTitle) {
        this.id = id;
        this.thesisId = thesisId;
        this.averageScore = averageScore;
        this.chairmanComment = chairmanComment;
        this.thesisTitle = thesisTitle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getThesisId() {
        return thesisId;
    }

    public void setThesisId(Integer thesisId) {
        this.thesisId = thesisId;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }

    public String getChairmanComment() {
        return chairmanComment;
    }

    public void setChairmanComment(String chairmanComment) {
        this.chairmanComment = chairmanComment;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    @Override
    public String toString() {
        return "EvaluationFinalScoreDTO{" +
                "id=" + id +
                ", thesisId=" + thesisId +
                ", averageScore=" + averageScore +
                ", chairmanComment='" + chairmanComment + '\'' +
                '}';
    }
} 