package com.app.bank.BankApplication.DAO;

import com.app.bank.BankApplication.Entity.AdminDetails;
import com.app.bank.BankApplication.Entity.CustomerDetails;
import com.app.bank.BankApplication.Entity.TransactionDetails;
import java.util.List;

public interface BankDao {
//    save details and history
    void saveDetails(CustomerDetails customerDetails);
    void saveTransactionHistory(TransactionDetails transactionDetails);

//    Create admin account
    public void createAdminAccount(AdminDetails adminDetails);
//    find the CustomerDetails and Transaction details and balance details and Admin
    CustomerDetails bankDetailsById(int theId);
    CustomerDetails findCustomerDetailsByAccountNumber(long accountNumber);
    List<TransactionDetails> findTransactionDetailsById(int transactionId);
    double getBalanceDetailsById(int id);
    AdminDetails findAdminDetailsById(int theId);
//   Merge Customer details and Admin Details
    void mergeCustomerDetails(CustomerDetails customerDetails);
    void mergeAdminDetails(AdminDetails adminDetails);
//    delete the customer Details
    void bankDetailsDelete(CustomerDetails customerDetails);
//    get all the customer Details
    List<CustomerDetails> getCustomerRecords();

}
