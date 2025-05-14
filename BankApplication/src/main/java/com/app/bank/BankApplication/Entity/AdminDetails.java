package com.app.bank.BankApplication.Entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "admin_details")
public class AdminDetails {
//    Declare the fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password")
    private String adminPassword;
    private String confirmAdminPassword;
    private String role;
    public AdminDetails(){}

//  Declare constructor and getters and setters
    public AdminDetails(String adminName, String adminPassword , String role) {
        this.lastName = adminName;
        this.adminPassword = adminPassword;
        this.role = role;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(adminPassword);
        this.adminPassword = encodePassword;
    }

    public String getConfirmAdminPassword() {
        return confirmAdminPassword;
    }

    public void setConfirmAdminPassword(String confirmAdminPassword) {
        this.confirmAdminPassword = confirmAdminPassword;
    }
}
