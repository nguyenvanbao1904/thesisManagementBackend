/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.EvaluationCriteria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @JsonIgnore
    private AcademicStaff createdBy;

    @NotEmpty(message = "{evaluationCriteriaCollection.evaluationCriterias.notnullMsg}")
    private List<EvaluationCriteria> selectedCriterias;

    public EvaluationCriteriaCollectionDTO() {
    }

    public EvaluationCriteriaCollectionDTO(Integer id, String name, String description, List<EvaluationCriteriaDTO> evaluationCriterias, AcademicStaff createdBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.evaluationCriterias = evaluationCriterias;
        this.createdBy = createdBy;
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

    public AcademicStaff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AcademicStaff createdBy) {
        this.createdBy = createdBy;
    }

    public List<EvaluationCriteria> getSelectedCriterias() {
        return selectedCriterias;
    }

    public void setSelectedCriterias(List<EvaluationCriteria> selectedCriterias) {
        this.selectedCriterias = selectedCriterias;
    }
}
