package com.app.bank.BankApplication.Security;

import com.app.bank.BankApplication.Entity.AdminDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminDetails, Integer> {
     AdminDetails findByLastName(String lastName);
}
