/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author nguyenvanbao
 */
public class EvaluationCriteriaDTO {
    private Integer id;
    @NotBlank(message = "{evaluationCriteria.name.notBlankMsg}")
    private String name;
    @NotBlank(message = "{evaluationCriteria.description.notBlankMsg}")
    private String description;
    @NotNull(message = "{evaluationCriteria.maxPoint.notNullMsg}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{evaluationCriteria.maxPoint.minExclusiveMsg}")
    private Float maxPoint;
    @DecimalMin(value = "0.0", inclusive = true, message = "{evaluationCriteria.weight.minExclusiveMsg}")
    @DecimalMax(value = "1.0", inclusive = true, message = "{evaluationCriteria.weight.maxMsg}")
    private Float weight;

    public EvaluationCriteriaDTO(Integer id, String name, String description, Float maxPoint, Float weight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxPoint = maxPoint;
        this.weight = weight;
    }

    public EvaluationCriteriaDTO(Integer id, String name, String description, Float maxPoint) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxPoint = maxPoint;
    }
    
    public EvaluationCriteriaDTO() {
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

    public Float getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(Float maxPoint) {
        this.maxPoint = maxPoint;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

   
}
