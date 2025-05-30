/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 *
 * @author nguyenvanbao
 */
public class EvaluationCriteriaCollectionDTO {

    private Integer id;
    
    @NotBlank(message = "{evaluationCriteriaCollection.name.notnullMsg}")
    private String name;
    
    @NotBlank(message = "{evaluationCriteriaCollection.description.notnullMsg}")
    private String description;
    
    private List<EvaluationCriteriaDTO> evaluationCriterias;
    
    private Integer[] selectedCriteriaIds;
    
    // Thông tin bổ sung cho hiển thị
    private String createdByName;
    private Integer createdById;

    public EvaluationCriteriaCollectionDTO() {
    }

    public EvaluationCriteriaCollectionDTO(Integer id, String name, String description, Integer[] selectedCriteriaIds, String createdByName, Integer createdById) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.selectedCriteriaIds = selectedCriteriaIds;
        this.createdByName = createdByName;
        this.createdById = createdById;
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

    public List<EvaluationCriteriaDTO> getEvaluationCriterias() {
        return evaluationCriterias;
    }

    public void setEvaluationCriterias(List<EvaluationCriteriaDTO> evaluationCriterias) {
        this.evaluationCriterias = evaluationCriterias;
    }

    public Integer[] getSelectedCriteriaIds() {
        return selectedCriteriaIds;
    }

    public void setSelectedCriteriaIds(Integer[] selectedCriteriaIds) {
        this.selectedCriteriaIds = selectedCriteriaIds;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    @Override
    public String toString() {
        return "EvaluationCriteriaCollectionDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", selectedCriteriaIds=" + (selectedCriteriaIds != null ? selectedCriteriaIds.length : "null") +
                '}';
    }
}
