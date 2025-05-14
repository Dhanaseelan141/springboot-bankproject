package com.app.bank.BankApplication.Controller;
import com.app.bank.BankApplication.Entity.*;
import com.app.bank.BankApplication.Service.BankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/bank")
public class BankController {
//    Inject the BankService
    private BankService bankService;
    public BankController(BankService bankService){
        this.bankService = bankService;
    }

    @GetMapping("/login")
    public String login(){
        return "sign-in";
    }
//    logout
    @GetMapping("/logout")
    public String logout(){
        return "sign-in";
    }


    @GetMapping("/register")
    public String customerRegister(Model model){
        CustomerDetails customerDetails = new CustomerDetails();
        model.addAttribute("customer" , customerDetails);
        return "create-account";
    }
    @PostMapping("/create")
    public String createAccount(@ModelAttribute("customer") CustomerDetails customerDetails){
//        Random object create and get Today date and format date
        Random rand = new Random();
        LocalDate today = LocalDate.now();
        Date formattedDate = Date.valueOf(today);

//        Create list and set a value
        List<AccountDetails> accountDetailsList = new ArrayList<>();
        long cifNumber = ThreadLocalRandom.current().nextLong(10000000000l ,99999999999l);
        accountDetailsList.add(new AccountDetails(ThreadLocalRandom.current().nextLong(10000000000000l ,99999999999999l) ,cifNumber,formattedDate ));
        accountDetailsList.add(new AccountDetails(ThreadLocalRandom.current().nextLong(10000000000000l ,99999999999999l) ,cifNumber,formattedDate ));
//      AccountDetails linked to customerDetails
        for(AccountDetails accountDetail : accountDetailsList){
            accountDetail.setCustomerDetails(customerDetails);
        }

//   BalanceDeatails object creation and link to customerDetails
        BalanceDetails balanceDetails = new BalanceDetails(1000.00,rand.nextInt(1000), 0 ,0 , 600 +rand.nextInt(300));
        balanceDetails.setCustomerDetails(customerDetails);
//   AccountDetails and BalanceDetails instance set to customerDetails
        customerDetails.setAccountDetails(accountDetailsList);
        customerDetails.setBalanceDetails(balanceDetails);
//        Save to the database
        bankService.save(customerDetails);
        return "redirect:/bank/login";
    }
//    Home page
  @GetMapping("/home")
  public String homePage(HttpSession session , Model model){
        Integer customerId = (Integer) session.getAttribute("customerId");
        if(customerId == null){
            return "redirect:/bank/login?sessionExpired=true";
        }

        CustomerDetails customerDetails = bankService.findDetailsById(customerId);
        List<TransactionDetails> transactionDetails = bankService.findTransactionById(customerId);
//        Add the model attribute
        model.addAttribute("customerDetail" , customerDetails);
        model.addAttribute("balanceDetail" , customerDetails.getBalanceDetails());
        model.addAttribute("accountDetail" , customerDetails.getAccountDetails());
        model.addAttribute("transactionDetails" , transactionDetails);
        return "main-page";
  }
//  Transfer Fund
    @PostMapping("/transfer/fund")
    public String transferFund(@RequestParam int id , @RequestParam long accountNumber ,
                               @RequestParam long confirmAccountNumber , @RequestParam double amount ,
                               @RequestParam long account){
//      Check Account number and Re-enter Account number are same
        if(accountNumber != confirmAccountNumber){
            return "redirect:/bank/home?confirmAccount";
        }
//        Find sender customeDetails in the database
        CustomerDetails senderCustomerDetails = bankService.findDetailsById(id);
        if(senderCustomerDetails == null){
            throw new RuntimeException("User not found");
        }
//        Find receiver customerDetails in the database
        CustomerDetails receiverCustomerDetails = bankService.findAccountDetails(accountNumber);
        System.out.println("====> " + receiverCustomerDetails);
        if(receiverCustomerDetails == null){
            return "redirect:/bank/home?invalidreceiver";
        }


//        Declare TransactionDetails and Date and format
        TransactionDetails transaction = null ;
        LocalDate today = LocalDate.now();
        Date formattedDate = Date.valueOf(today);

//      get sender and receiver details
        long senderAccountNumber =  senderCustomerDetails.getAccountDetails().get(0).getAccountNumber();
        long senderCreditCardNumber = senderCustomerDetails.getAccountDetails().get(1).getAccountNumber();
        long receiverAccountNumber = receiverCustomerDetails.getAccountDetails().get(0).getAccountNumber();
        System.out.println("receiverAccountNumber ====>" + receiverAccountNumber);
        List<AccountDetails> receiverAccounts = receiverCustomerDetails.getAccountDetails();
        System.out.println("receiverAccounts ====>" + receiverAccounts);
        if (receiverAccounts.size() < 2) {
            return "redirect:/bank/home?missingreceivercredit";
        }
        long receiverCreditCardNumber = receiverCustomerDetails.getAccountDetails().get(1).getAccountNumber();

        double senderAccountBalance = senderCustomerDetails.getBalanceDetails().getAccountBalance();
        double senderCreditBalance = senderCustomerDetails.getBalanceDetails().getCreditCardBalance();
        double receiverAccountBalance = receiverCustomerDetails.getBalanceDetails().getAccountBalance();
        double receiverCreditBalance = receiverCustomerDetails.getBalanceDetails().getCreditCardBalance();

//        Check the account number
        if(account == senderAccountNumber) return handleFromAccountTransfer(id,
             accountNumber , amount , account , formattedDate ,
                senderAccountNumber , senderCreditCardNumber, receiverAccountNumber ,
                receiverCreditCardNumber ,senderAccountBalance , senderCreditBalance ,
                receiverAccountBalance , receiverCreditBalance ,  transaction ,
                senderCustomerDetails , receiverCustomerDetails
                );

        if(account == senderCreditCardNumber) return handleFromCreditCard(id,
                accountNumber , amount , account , formattedDate ,
                senderAccountNumber , senderCreditCardNumber, receiverAccountNumber ,
                receiverCreditCardNumber ,senderAccountBalance , senderCreditBalance ,
                receiverAccountBalance , receiverCreditBalance , transaction ,
                senderCustomerDetails , receiverCustomerDetails);

        return "redirect:/bank/home";
    }
//    payment using account number
    private String handleFromAccountTransfer(int id,
                                             long accountNumber , double amount , long account , Date formattedDate ,
                                             long senderAccountNumber , long senderCreditCardNumber, long receiverAccountNumber ,
                                             long receiverCreditCardNumber , double senderAccountBalance ,double senderCreditBalance ,
                                             double receiverAccountBalance , double receiverCreditBalance , TransactionDetails transaction ,
                                             CustomerDetails senderCustomerDetails , CustomerDetails receiverCustomerDetails) {
        if(accountNumber == senderAccountNumber ){
            return "redirect:/bank/home?same";
        }

        if(senderAccountBalance < amount){
            transaction = new TransactionDetails(formattedDate, "CC Payment" , amount , "Incomplete" , id);
            bankService.saveTransactionDetails(transaction);
            return "redirect:/bank/home?insufficient" ;
        }

        if(accountNumber == senderCreditCardNumber &&
              amount >= senderCustomerDetails.getBalanceDetails().getOutstandingBalance() ){

            if(senderCustomerDetails.getBalanceDetails().getCreditCardBalance() <= 0
                    && senderCustomerDetails.getBalanceDetails().getOutstandingBalance() <= 0){
                return "redirect:/bank/home?activatecredit" ;
            }

            payment(senderCustomerDetails ,senderAccountBalance , amount);
            senderCustomerDetails.getBalanceDetails().setCreditCardBalance(senderCreditBalance + amount);
            senderCustomerDetails.getBalanceDetails().setOutstandingBalance(senderCustomerDetails.getBalanceDetails().getOutstandingBalance() - amount);
            bankService.merge(senderCustomerDetails);
            creditTransaction(formattedDate , amount , transaction , id);
            return "redirect:/bank/home" ;
        }

        if(receiverAccountNumber == accountNumber){
            payment(senderCustomerDetails ,senderAccountBalance , amount);
            receiverCustomerDetails.getBalanceDetails().setAccountBalance(receiverAccountBalance + amount);
            bankService.merge(senderCustomerDetails);
            bankService.merge(receiverCustomerDetails);
            accountTransaction(formattedDate , amount , transaction , id);
            return "redirect:/bank/home" ;

        }
        if(receiverCreditCardNumber == accountNumber &&
                receiverCustomerDetails.getBalanceDetails().getOutstandingBalance() <= amount){

            if(receiverCustomerDetails.getBalanceDetails().getCreditCardBalance() <= 0
                    && receiverCustomerDetails.getBalanceDetails().getOutstandingBalance() <= 0){
                return "redirect:/bank/home?receivercredit" ;
            }

            payment(senderCustomerDetails ,senderAccountBalance , amount);
            receiverCustomerDetails.getBalanceDetails().setCreditCardBalance(receiverCreditBalance + amount);
            receiverCustomerDetails.getBalanceDetails().setOutstandingBalance(receiverCustomerDetails.getBalanceDetails().getOutstandingBalance() -  amount);
            bankService.merge(senderCustomerDetails);
            bankService.merge(receiverCustomerDetails);
            accountTransaction(formattedDate , amount , transaction , id);
            return "redirect:/bank/home" ;
        }
        return "redirect:/bank/home";

    }
//    Payment using credit card
    private String handleFromCreditCard(int id,
                                        long accountNumber , double amount , long account , Date formattedDate ,
                                        long senderAccountNumber , long senderCreditCardNumber, long receiverAccountNumber ,
                                        long receiverCreditCardNumber , double senderAccountBalance ,double senderCreditBalance ,
                                        double receiverAccountBalance , double receiverCreditBalance , TransactionDetails transaction ,
                                        CustomerDetails senderCustomerDetails , CustomerDetails receiverCustomerDetails) {
        if(accountNumber == senderCreditCardNumber ){
            return "redirect:/bank/home?same";
        }

        if(senderCreditBalance < amount){
            transaction = new TransactionDetails(formattedDate, "CC Payment" , amount , "Incomplete" , id);
            bankService.saveTransactionDetails(transaction);
            return "redirect:/bank/home?insufficient" ;
        }

        if(accountNumber == senderAccountNumber){

            creditPayment(senderCustomerDetails ,senderCreditBalance , amount);
            senderCustomerDetails.getBalanceDetails().setAccountBalance(senderAccountBalance + amount);
            bankService.merge(senderCustomerDetails);
            creditTransaction(formattedDate , amount , transaction , id);
            return "redirect:/bank/home" ;
        }

        if(receiverAccountNumber == accountNumber){
            creditPayment(senderCustomerDetails ,senderCreditBalance , amount);
            receiverCustomerDetails.getBalanceDetails().setAccountBalance(receiverAccountBalance + amount);
            bankService.merge(senderCustomerDetails);
            bankService.merge(receiverCustomerDetails);
            accountTransaction(formattedDate , amount , transaction ,id);
            return "redirect:/bank/home" ;

        }
        if(receiverCreditCardNumber == accountNumber &&
                receiverCustomerDetails.getBalanceDetails().getOutstandingBalance() <= amount){

            if(receiverCustomerDetails.getBalanceDetails().getCreditCardBalance() <= 0
                    && receiverCustomerDetails.getBalanceDetails().getOutstandingBalance() <= 0){
                return "redirect:/bank/home?receivercredit" ;
            }

            creditPayment(senderCustomerDetails ,senderCreditBalance , amount);
            receiverCustomerDetails.getBalanceDetails().setCreditCardBalance(receiverCreditBalance + amount);
            receiverCustomerDetails.getBalanceDetails().setOutstandingBalance(receiverCustomerDetails.getBalanceDetails().getOutstandingBalance() -  amount);
            bankService.merge(senderCustomerDetails);
            bankService.merge(receiverCustomerDetails);
            accountTransaction( formattedDate , amount , transaction , id);
            return "redirect:/bank/home" ;
        }
        return "redirect:/bank/home";
    }
//    Normal bank payment
    private void payment(CustomerDetails senderCustomerDetails , double senderAccountBalance , double amount){
        senderCustomerDetails.getBalanceDetails().setAccountBalance(senderAccountBalance - amount);
    }
//    Payment using credit card
    private void creditPayment(CustomerDetails senderCustomerDetails , double senderCreditBalance , double amount){
        senderCustomerDetails.getBalanceDetails().setCreditCardBalance(senderCreditBalance - amount);
        senderCustomerDetails.getBalanceDetails().setOutstandingBalance(senderCustomerDetails.getBalanceDetails().getOutstandingBalance() + amount);
    }
//    Add a Tranaction history
    private void creditTransaction( Date formattedDate , double amount , TransactionDetails transaction , int id ){
        transaction = new TransactionDetails(formattedDate, "CC Payment" , amount , "Completed" , id );
        bankService.saveTransactionDetails(transaction);
    }
    private void accountTransaction( Date formattedDate , double amount , TransactionDetails transaction , int id ){
        transaction = new TransactionDetails(formattedDate, "transfer" , amount , "Completed" , id );
        bankService.saveTransactionDetails(transaction);
    }


//  credit cardw
    @PatchMapping("/home/credit/apply")
    public String creditCardApply(@RequestParam int id , @RequestParam double salary ,@RequestParam double creditcard , Model model){
//        if salary less than 20000 . not eligible for credit card
        if(salary <= 20000 && creditcard == 0){
           return "redirect:/bank/home?creditcard";
        }
        CustomerDetails customer = bankService.findDetailsById(id);
        if(customer == null){
            throw new RuntimeException("Customer id not found " + id);
        }
        if(customer.getBalanceDetails().getCreditCardBalance() < 1){
            customer.getBalanceDetails().setCreditCardBalance(salary * 2);
            bankService.merge(customer);
            return "redirect:/bank/home";
        }

        return "redirect:/bank/home?maximumlimit";
    }
//    deposit
    @PostMapping("/deposit")
    public String depositAmount(@RequestParam int id , @RequestParam long accountNumber , @RequestParam double amount){
        if(amount > 2000000 && amount <= 0){
            return "redirect:/bank/home?maximum" ;
        }
//        TransactionDetails object and date
        TransactionDetails transaction;
        LocalDate today = LocalDate.now();
        Date formattedDate = Date.valueOf(today);

        CustomerDetails customerDetails = bankService.findDetailsById(id);
        if(customerDetails == null){
            throw new RuntimeException("Customer id not found " + id);
        }
//        Check the account number is normal bank account

        if(customerDetails.getAccountDetails().get(0).getAccountNumber() == accountNumber){

            customerDetails.getBalanceDetails().setAccountBalance(amount);
            bankService.merge(customerDetails);

            transaction = new TransactionDetails(formattedDate, "Bank deposit" , amount , "Completed" , id );
            bankService.saveTransactionDetails(transaction);
            return "redirect:/bank/home";
        }
//        Check the credit card number
        if(customerDetails.getAccountDetails().get(1).getAccountNumber() == accountNumber
                    && customerDetails.getBalanceDetails().getOutstandingBalance() <= amount ) {
            if(customerDetails.getBalanceDetails().getOutstandingBalance() <= 0 &&
                    customerDetails.getBalanceDetails().getCreditCardBalance() <= 0){
                return "redirect:/bank/home?activatecredit";
            }
            customerDetails.getBalanceDetails().setCreditCardBalance(amount);
            customerDetails.getBalanceDetails().setOutstandingBalance(customerDetails.getBalanceDetails().getOutstandingBalance() - amount);
            bankService.merge(customerDetails);

            transaction = new TransactionDetails(formattedDate, "Credit Payment" , amount , "Completed" , id );
            bankService.saveTransactionDetails(transaction);
            return "redirect:/bank/home";
        }
        return "redirect:/bank/home?invalidaccount";
    }
//    Forgot Password
    @PostMapping("/forgotpassword")
    public String forgotUserPassword(@RequestParam int id , @RequestParam String password , @RequestParam String repassword){
        if(password.equals(repassword)){
            CustomerDetails customerDetails = bankService.findDetailsById(id);
            customerDetails.setPassword(password);
            bankService.merge(customerDetails);
            return "redirect:/bank/home";
        }
        return "redirect:/bank/home?invalidpassword";
    }
//    Account Closing
   @PostMapping("/close")
   public String closeAccount(@RequestParam int id , @RequestParam long accountNumber , @RequestParam String reason ){
        CustomerDetails customerDetails = bankService.findDetailsById(id);
        if(customerDetails == null){
            throw new RuntimeException("user not found");
        }
//        change the account opening status
        customerDetails.setOpening_action("CLOSE_REQUEST");
        customerDetails.setReason(reason);
        bankService.merge(customerDetails);
        return "redirect:/bank/home";
   }

//    Admin page Controller
    @GetMapping("/admin")
    public String adminPanel(HttpSession session , Model model){
        String username = (String) session.getAttribute("managerUsername");
        int id = (int) session.getAttribute("id");
        if(username == null){
            return "redirect:/bank/login?sessionExpired=true";
        }
        model.addAttribute("username" , username);
        model.addAttribute("id" , id);
        return "admin-panel";
    }
//    Admin account create page
    @GetMapping("/admin/create")
    public String createAdminAccount(Model model) {
        AdminDetails adminDetails = new AdminDetails();
        model.addAttribute("admin" , adminDetails);
        return "admin-create-account";
    }
//    Admin account stored to database
   @PostMapping("/admin/create/save")
   public String saveAdminAccount(@ModelAttribute("admin") AdminDetails adminDetails , Model model){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        Check the password and confirm password same
        if(!encoder.matches(adminDetails.getConfirmAdminPassword() , adminDetails.getAdminPassword())){
            model.addAttribute("error" , "Password and Confirm Password must be same! ..");
            return "admin-create-account";
        }
        adminDetails.setConfirmAdminPassword(adminDetails.getAdminPassword());
//        Save to the database
        bankService.saveAdminDetails(adminDetails);
        return "redirect:/bank/admin";
   }
//   Admin password reset page
    @GetMapping("/forgotadmin/{id}")
    public String forgotAdminPage(@PathVariable int id , Model model){
        model.addAttribute("id" , id);
        return "forgot";
    }

//  Admin password reset
    @PostMapping("/password/save")
    public String saveAdminPassword(@RequestParam int id ,@RequestParam String password , @RequestParam String repassword){
        if(password.equals(repassword)){
            AdminDetails admin = bankService.findAdminById(id);
            admin.setAdminPassword(password);
            bankService.mergeAdmin(admin);
            return "redirect:/bank/admin";
        }
        return "redirect:/bank/admin?invalidadminpassword";
    }
// show the admin balance update form
    @GetMapping("/user/balance/{id}")
    public String updateBalance(@PathVariable int id , Model model){
        double accountBalance =  bankService.findAccountBalance(id);
        model.addAttribute("accountBalances" , accountBalance);
        model.addAttribute("id" , id);
        return "admin-update-balance";
    }
//    Save the balance update
    @PatchMapping("/users/balanceamount/save")
    public String saveBalance(@RequestParam int id , double accountBalance){
        CustomerDetails customerDetails = bankService.findDetailsById(id);
        customerDetails.getBalanceDetails().setAccountBalance(accountBalance);
        bankService.merge(customerDetails);
        return "redirect:/bank/admin";
    }
//    initial customer account opening approval
    @GetMapping("/account/Approval/{id}")
    public String approval(@PathVariable int id){
        CustomerDetails customerDetails = bankService.findDetailsById(id);
        customerDetails.setOpening_action("APPROVED");
        bankService.merge(customerDetails);
        return "redirect:/bank/admin";
    }
//    close the account
    @GetMapping("/decline/{id}")
    public String decline(@PathVariable int id){
        CustomerDetails customerDetails = bankService.findDetailsById(id);
        List<AccountDetails> accountDetails = customerDetails.getAccountDetails();
        BalanceDetails balanceDetails = customerDetails.getBalanceDetails();
        for(AccountDetails accountDetail : accountDetails){
            accountDetail.setCustomerDetails(null);
        }
        bankService.deleteDetails(customerDetails);
        return "redirect:/bank/admin";
    }
// reject the account closing
    @GetMapping("/reject/{id}")
    public String reject(@PathVariable int id){
        CustomerDetails customerDetails = bankService.findDetailsById(id);
        customerDetails.setOpening_action("APPROVED");
        bankService.merge(customerDetails);
        return "redirect:/bank/admin";
    }


}






