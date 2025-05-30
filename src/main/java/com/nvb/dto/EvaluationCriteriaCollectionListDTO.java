/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

/**
 *
 * @author nguyenvanbao
 */
public class EvaluationCriteriaCollectionListDTO {

    private Integer id;

    private String name;

    private String description;

    private String createdByName;

    public EvaluationCriteriaCollectionListDTO(Integer id, String name, String description, String createdByName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdByName = createdByName;
    }

    public EvaluationCriteriaCollectionListDTO() {
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

}
