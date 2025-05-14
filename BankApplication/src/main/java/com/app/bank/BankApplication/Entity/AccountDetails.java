package com.app.bank.BankApplication.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name="account_details")
@JsonIgnoreProperties({"customerDetails"})
public class AccountDetails {
//    Declare the CustomerDetails object variable
    @ManyToOne
    @JoinColumn(name="customer_id_ref" , nullable = false)
    private CustomerDetails customerDetails;
//    Constructor
    public AccountDetails(){

    }
    public AccountDetails(long accountNumber, long cifNumber, Date dateOfOpening ) {
        this.accountNumber = accountNumber;
        this.cifNumber = cifNumber;
        this.dateOfOpening = dateOfOpening;
    }
//    Declare the fields

    @Id
    @Column(name="account_number")
    private long accountNumber;

    @Column(name = "cif_number")
    private long cifNumber;
    @Column(name = "date_of_opening")
    private Date dateOfOpening;

//    Getters and Setter and toString
    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getCifNumber() {
        return cifNumber;
    }

    public void setCifNumber(long cifNumber) {
        this.cifNumber = cifNumber;
    }

    public Date getDateOfOpening() {
        return dateOfOpening;
    }

    public void setDateOfOpening(Date dateOfOpening) {
        this.dateOfOpening = dateOfOpening;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "accountNumber=" + accountNumber +
                ", cifNumber=" + cifNumber +
                ", dateOfOpening=" + dateOfOpening +
                '}';
    }
}
