package com.nvb.dto;

import jakarta.validation.constraints.NotBlank;

public class MajorDTO {
    private Integer id;
    @NotBlank(message = "{major.name.notnullMsg}")
    private String name;
    private Boolean isActive;

    public MajorDTO() {}

    public MajorDTO(Integer id, String name, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
} 