package com.app.bank.BankApplication.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    Declare JdbcTemplate and inject them
    private JdbcTemplate jdbcTemplate;

    public CustomAuthenticationSuccessHandler(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
//        Check the customer_details table
        List<Map<String, Object>> customers = jdbcTemplate.queryForList(
                "SELECT customer_id,role FROM customer_details WHERE email = ?" , username
        );
//       Check the customerDetails table not empty
        if(!customers.isEmpty()){
//            get customerId and set session attribute
            int customerId = (int) customers.get(0).get("customer_id");
            request.getSession().setAttribute("customerId" , customerId);
//            Redirect to home page
            response.sendRedirect("/bank/home");
            return;

        }
        //    Check the admin_details table
        List<Map<String, Object>> managers = jdbcTemplate.queryForList(
                "SELECT id,role FROM admin_details WHERE last_name = ?" , username
        );
//        check admin table not empty
        if(!managers.isEmpty()){
            int managerId = (int) managers.get(0).get("id");
            request.getSession().setAttribute("managerUsername" , username);
            request.getSession().setAttribute("id" , managerId);
            response.sendRedirect("/bank/admin");
            return;
        }
        response.sendRedirect("/login?error=true");
    }

}

