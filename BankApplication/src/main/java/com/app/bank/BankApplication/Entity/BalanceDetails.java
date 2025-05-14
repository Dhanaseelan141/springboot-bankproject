package com.app.bank.BankApplication.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="balance_details")
@JsonIgnoreProperties({"customerDetails"})
public class BalanceDetails {
//    Declare the fields
    @OneToOne
    @JoinColumn(name = "customer_id_ref" , nullable = false)
    private CustomerDetails customerDetails;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "account_balance")
    private double accountBalance;
    @Column(name = "lien_balance")
    private double lienBalance;
    @Column(name = "credit_card_balance")
    private double creditCardBalance;
    @Column(name = "outstanding_balance")
    private double outstandingBalance;
    @Column(name = "credit_score")
    private int creditScore;

//    Declare the constructor

    public BalanceDetails(){

    }
    public BalanceDetails(double accountBalance, double lienBalance, double creditCardBalance, double outstandingBalance, int creditScore) {
        this.accountBalance = accountBalance;
        this.lienBalance = lienBalance;
        this.creditCardBalance = creditCardBalance;
        this.outstandingBalance = outstandingBalance;
        this.creditScore = creditScore;
    }
//  Getters and setters method
    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getLienBalance() {
        return lienBalance;
    }

    public void setLienBalance(double lienBalance) {
        this.lienBalance = lienBalance;
    }

    public double getCreditCardBalance() {
        return creditCardBalance;
    }

    public void setCreditCardBalance(double creditCardBalance) {
        this.creditCardBalance = creditCardBalance;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    @Override
    public String toString() {
        return "BalanceDetails{" +
                "accountBalance=" + accountBalance +
                ", lienBalance=" + lienBalance +
                ", creditCardBalance=" + creditCardBalance +
                ", outstandingBalance=" + outstandingBalance +
                ", creditScore=" + creditScore +
                '}';
    }
}
