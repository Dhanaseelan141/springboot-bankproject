package com.app.bank.BankApplication.DAO;

import com.app.bank.BankApplication.Entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class BankDaoImpl implements BankDao{
    private EntityManager entityManager;
    public BankDaoImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }
//    Save details and history
    public void saveDetails(CustomerDetails customerDetails){
        entityManager.persist(customerDetails);
    }
    public void saveTransactionHistory(TransactionDetails transactionDetails){
        entityManager.persist(transactionDetails);
    }
//    Merge Customer Details and Admin Details
    @Override
    public void mergeCustomerDetails(CustomerDetails customerDetails){
        entityManager.merge(customerDetails);
    }
    @Override
    public void mergeAdminDetails(AdminDetails adminDetails){
        entityManager.merge(adminDetails);
    }
//   Create a admin account
    @Override
    public void createAdminAccount(AdminDetails adminDetails) {
        entityManager.persist(adminDetails);
    }
//    find Customer Details and Transaction Details and admin details
    public CustomerDetails bankDetailsById(int theId){
        return entityManager.find(CustomerDetails.class , theId);
    }
    public List<TransactionDetails> findTransactionDetailsById(int transactionId){
        TypedQuery theQuery = entityManager.createQuery(
                "SELECT t FROM TransactionDetails t WHERE t.transactionId = :id" , TransactionDetails.class
        );
        theQuery.setParameter("id" , transactionId);
        return  theQuery.getResultList();
    }
    @Override
    public AdminDetails findAdminDetailsById(int theId){
        return entityManager.find(AdminDetails.class , theId);
    }

//    find the customer Details using account number
    public CustomerDetails  findCustomerDetailsByAccountNumber(long accountNumber){
//        Create a query
        try{

            AccountDetails acc = entityManager.createQuery(
                            "SELECT a FROM AccountDetails a WHERE a.accountNumber = :accountNumber", AccountDetails.class)
                    .setParameter("accountNumber", accountNumber)
                    .getSingleResult();

            return acc.getCustomerDetails();
        } catch(NoResultException e){
            return null;
        }

    }
//    delete the customer Details
    public void bankDetailsDelete(CustomerDetails customerDetails){
        entityManager.remove(customerDetails);
    }
//    get all the customer Details
    public List<CustomerDetails> getCustomerRecords(){
//        Create a query
        TypedQuery<CustomerDetails> theQuery = entityManager.createQuery("SELECT c FROM CustomerDetails c " +
                        "LEFT JOIN FETCH c.accountDetails a " + "LEFT JOIN FETCH c.balanceDetails b",
               CustomerDetails.class );
//        execute query and get result list
        List<CustomerDetails> customer = theQuery.getResultList();
//        return the result
        return customer;

    }
//    Update Balance
    public double getBalanceDetailsById(int id){
        TypedQuery<Double> balanceQuery = entityManager.createQuery("SELECT b.accountBalance FROM BalanceDetails b where b.id = :id" , Double.class);
        balanceQuery.setParameter("id" ,id);
        return balanceQuery.getSingleResult();
    }


}
