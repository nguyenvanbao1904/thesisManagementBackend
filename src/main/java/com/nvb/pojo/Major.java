/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "major")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Major.findAll", query = "SELECT m FROM Major m"),
    @NamedQuery(name = "Major.findById", query = "SELECT m FROM Major m WHERE m.id = :id"),
    @NamedQuery(name = "Major.findByName", query = "SELECT m FROM Major m WHERE m.name = :name"),
    @NamedQuery(name = "Major.findByIsActive", query = "SELECT m FROM Major m WHERE m.isActive = :isActive")})
public class Major implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive = true;
    @OneToMany(mappedBy = "major")
    private Set<Student> students;

    public Major() {
    }

    public Major(Integer id) {
        this.id = id;
    }

    public Major(Integer id, boolean isActive) {
        this.id = id;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    public Set<Student> getstudents() {
        return students;
    }

    public void setstudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Major)) {
            return false;
        }
        Major other = (Major) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.Major[ id=" + id + " ]";
    }
    
}
