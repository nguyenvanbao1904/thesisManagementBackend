package com.nvb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class CommitteeListDTO {

    private Integer id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime defenseDate;
    private String location;
    private String status;
    private Boolean isActive;
    private String createdByName; // Tên người tạo

    // Constructors
    public CommitteeListDTO() {
    }

    public CommitteeListDTO(Integer id, LocalDateTime defenseDate, String location, String status, Boolean isActive, String createdByName) {
        this.id = id;
        this.defenseDate = defenseDate;
        this.location = location;
        this.status = status;
        this.isActive = isActive;
        this.createdByName = createdByName;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDateTime defenseDate) {
        this.defenseDate = defenseDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
}
