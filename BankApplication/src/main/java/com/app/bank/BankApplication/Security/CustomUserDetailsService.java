package com.app.bank.BankApplication.Security;

import com.app.bank.BankApplication.Entity.AdminDetails;
import com.app.bank.BankApplication.Entity.CustomerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
//  Inject the BankRepository and AdminRepository
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Find the username using email
        CustomerDetails customer = bankRepository.findByEmail(username);
//        check the customer is not null
        if(customer != null) {
            return new User(
                    customer.getEmail(),
                    customer.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(customer.getRole()))
            );
        }
//        find the admin using last name
        AdminDetails admin =  adminRepository.findByLastName(username);
        if(admin!= null){
            return new User(
                    admin.getLastName(),
                    admin.getAdminPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(admin.getRole()))
            );
        }
       throw new UsernameNotFoundException("User not found");
    }
}

