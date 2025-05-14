package com.app.bank.BankApplication.Rest;

import com.app.bank.BankApplication.Entity.CustomerDetails;
import com.app.bank.BankApplication.Service.BankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BankRest {
    private BankService bankService;

    public BankRest(BankService bankService){
        this.bankService = bankService;
    }
//    All the customer Details show in the path
    @GetMapping("/customer")
    public List<CustomerDetails> allCustomerDetails(){
        return bankService.getAllCustomerDetails();
    }
}
