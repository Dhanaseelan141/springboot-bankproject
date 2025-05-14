package com.app.bank.BankApplication.Entity;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name="transaction_details")
public class TransactionDetails {
//    Declare the fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "date")
    private Date date;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private double amount;
    @Column(name = "status")
    private String status;
    @Column(name = "transaction_no")
    private int transactionId;
//  Constructor
    public TransactionDetails(){}


    public TransactionDetails(Date date, String description, double amount, String status , int transactionId) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.transactionId = transactionId;
    }
// Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
