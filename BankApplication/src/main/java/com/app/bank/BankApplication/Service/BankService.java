package com.app.bank.BankApplication.Service;

import com.app.bank.BankApplication.Entity.AdminDetails;
import com.app.bank.BankApplication.Entity.CustomerDetails;
import com.app.bank.BankApplication.Entity.TransactionDetails;
import java.util.List;

public interface BankService {
//    Save Details
    public void save(CustomerDetails customerDetails);
    public void saveTransactionDetails(TransactionDetails transactionDetails);
    public void saveAdminDetails(AdminDetails adminDetails);
//    Merge Details
    public void merge(CustomerDetails customerDetails);
    public void mergeAdmin(AdminDetails adminDetails);
//    find customer Details and transactionDetails and adminDetails
    public CustomerDetails findDetailsById(int theId);
    public CustomerDetails findAccountDetails(long accountNumber);
    public AdminDetails findAdminById(int theId);
    public List<TransactionDetails> findTransactionById(int transactionId);
    public List<CustomerDetails> getAllCustomerDetails();
    public double findAccountBalance(int theId);
//    delete customer Details
    public void deleteDetails(CustomerDetails customerDetails);
}
