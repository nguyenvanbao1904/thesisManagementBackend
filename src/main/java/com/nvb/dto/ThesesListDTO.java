package com.nvb.dto;


public class ThesesListDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private String fileUrl;

    public ThesesListDTO(Integer id, String title, String description, String status, String fileUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.fileUrl = fileUrl;
    }

    public ThesesListDTO() {
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
     
} 