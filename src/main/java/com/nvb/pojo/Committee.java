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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author nguyenvanbao
 */
@Entity
@Table(name = "committee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Committee.findAll", query = "SELECT c FROM Committee c"),
    @NamedQuery(name = "Committee.findById", query = "SELECT c FROM Committee c WHERE c.id = :id"),
    @NamedQuery(name = "Committee.findByDefenseDate", query = "SELECT c FROM Committee c WHERE c.defenseDate = :defenseDate"),
    @NamedQuery(name = "Committee.findByLocation", query = "SELECT c FROM Committee c WHERE c.location = :location"),
    @NamedQuery(name = "Committee.findByStatus", query = "SELECT c FROM Committee c WHERE c.status = :status"),
    @NamedQuery(name = "Committee.findByCreatedAt", query = "SELECT c FROM Committee c WHERE c.createdAt = :createdAt"),
    @NamedQuery(name = "Committee.findByUpdatedAt", query = "SELECT c FROM Committee c WHERE c.updatedAt = :updatedAt"),
    @NamedQuery(name = "Committee.findByIsActive", query = "SELECT c FROM Committee c WHERE c.isActive = :isActive")})
public class Committee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "defense_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime defenseDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @Basic(optional = false)
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private AcademicStaff createdBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "committee", orphanRemoval = true)
    private Set<CommitteeMember> committeeMembers;
    @OneToMany(mappedBy = "committeeId")
    private Set<Thesis> theses;

    public Committee() {
    }

    public Committee(Integer id) {
        this.id = id;
    }

    public Committee(Integer id, LocalDateTime defenseDate, String location, String status, Date createdAt, Date updatedAt, boolean isActive) {
        this.id = id;
        this.defenseDate = defenseDate;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public AcademicStaff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AcademicStaff createdBy) {
        this.createdBy = createdBy;
    }

    public Set<CommitteeMember> getCommitteeMembers() {
        return committeeMembers;
    }

    public void setCommitteeMembers(Set<CommitteeMember> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    public Set<Thesis> getTheses() {
        return theses;
    }

    public void setTheses(Set<Thesis> theses) {
        this.theses = theses;
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
        if (!(object instanceof Committee)) {
            return false;
        }
        Committee other = (Committee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvb.configs.Committee[ id=" + id + " ]";
    }
    
}
