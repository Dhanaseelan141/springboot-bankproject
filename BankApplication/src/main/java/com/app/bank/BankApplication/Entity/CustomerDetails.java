package com.app.bank.BankApplication.Entity;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_details")
public class CustomerDetails {
//  Declate the fields
    @OneToMany(mappedBy="customerDetails", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private List<AccountDetails> accountDetails;

    @OneToOne(mappedBy = "customerDetails", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private BalanceDetails balanceDetails;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    private int id;
    @Column(name="first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private double phoneNumber;
    @Column(name="dob")
    private Date dob;
    @Column(name = "pan")
    private String pan;
    @Column(name = "aadhar")
    private long aadhar;
    @Column(name = "password")
    private String password;
    private String role;
    private String opening_action;
    @Column(name = "reason")
    private String reason;

    //    Constructor
    public CustomerDetails(){

    }

    public CustomerDetails(String firstName, String lastName, String email, double phoneNumber, Date date, String pan, long aadhar, String password  , BalanceDetails balanceDetails ,String role ,String opening_action , String reason) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pan = pan;
        this.aadhar = aadhar;
        this.password = password;
        this.accountDetails = new ArrayList<AccountDetails>();
        this.balanceDetails = balanceDetails;
        this.role = role ;
        this.opening_action = opening_action ;
        this.reason = reason;
    }
//    Getters and setters

    public String getOpening_action() {
        return opening_action;
    }

    public void setOpening_action(String opening_action) {
        this.opening_action = opening_action;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(double phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public long getAadhar() {
        return aadhar;
    }

    public void setAadhar(long aadhar) {
        this.aadhar = aadhar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(password);
        this.password = encodePassword;
    }

    public String getFirstName(){
        return firstName;
    }

    public BalanceDetails getBalanceDetails() {
        return balanceDetails;
    }

    public void setBalanceDetails(BalanceDetails balanceDetails) {
        this.balanceDetails = balanceDetails;
    }

    public List<AccountDetails> getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(List<AccountDetails> accountDetails) {
        this.accountDetails = accountDetails;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CustomerDetails{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", dob=" + dob +
                ", pan='" + pan + '\'' +
                ", aadhar=" + aadhar +
                ", password='" + password + '\'' +
                '}';
    }


}
