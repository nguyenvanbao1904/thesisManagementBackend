/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.pojo;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "admin")
@XmlRootElement
@DiscriminatorValue("ROLE_ADMIN")
@NamedQueries({
    @NamedQuery(name = "Admin.findAll", query = "SELECT a FROM Admin a"),
    @NamedQuery(name = "Admin.findById", query = "SELECT a FROM Admin a WHERE a.id = :id")})
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    public Admin() {
    }

    public Admin(Integer id) {
        super(id);
    }
}
