package com.app.bank.BankApplication.Security;

import com.app.bank.BankApplication.Entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<CustomerDetails, Integer> {
    CustomerDetails findByEmail(String email);
}
