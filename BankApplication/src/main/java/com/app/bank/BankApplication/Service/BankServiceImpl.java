package com.app.bank.BankApplication.Service;

import com.app.bank.BankApplication.DAO.BankDao;
import com.app.bank.BankApplication.Entity.AdminDetails;
import com.app.bank.BankApplication.Entity.CustomerDetails;
import com.app.bank.BankApplication.Entity.TransactionDetails;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BankServiceImpl implements BankService {
    private BankDao theBankDao;
    public BankServiceImpl(BankDao theBankDao){
        this.theBankDao = theBankDao;
    }
//  find the customerDetails
    public CustomerDetails findDetailsById(int theId){
        return theBankDao.bankDetailsById(theId);
    }
    public CustomerDetails findAccountDetails(long accountNumber){
        return theBankDao.findCustomerDetailsByAccountNumber(accountNumber);
    }
    public List<TransactionDetails> findTransactionById(int transactionId){
        return theBankDao.findTransactionDetailsById(transactionId);
    }
    @Override
    public AdminDetails findAdminById(int theId){
        return theBankDao.findAdminDetailsById(theId);
    }
//    Save Details
    @Transactional
    public void save(CustomerDetails theCustomerDetails) {
        theBankDao.saveDetails(theCustomerDetails);
    }
    @Transactional
    public void saveTransactionDetails(TransactionDetails transactionDetails){
        theBankDao.saveTransactionHistory(transactionDetails);
    }
//    Merge Details
    @Transactional
    @Override
    public void merge(CustomerDetails customerDetails){
        theBankDao.mergeCustomerDetails(customerDetails);
    }
    @Transactional
    @Override
    public void mergeAdmin(AdminDetails adminDetails){
        theBankDao.mergeAdminDetails(adminDetails);
    }
//    save admin details
    @Transactional
    @Override
    public void saveAdminDetails(AdminDetails adminDetails) {
        theBankDao.createAdminAccount(adminDetails);
    }
//    delete customer details
    @Transactional
    @Override
    public void deleteDetails(CustomerDetails customerDetails){
        theBankDao.bankDetailsDelete(customerDetails);
    }
//    find all customerDetails
    public List<CustomerDetails> getAllCustomerDetails(){
       return theBankDao.getCustomerRecords();
    }
//    update balance
public double findAccountBalance(int id){
        return theBankDao.getBalanceDetailsById(id);
}

}
