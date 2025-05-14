package com.app.bank.BankApplication.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class BankSecurityConfig {
//    Declare and inject the CustomAuthenticationSuccessHandler

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public BankSecurityConfig(CustomAuthenticationSuccessHandler handler) {
        this.customAuthenticationSuccessHandler = handler;
    }
// filter the html hidden field
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
//    SecurityFilterChain handle request and authorize
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/bank/register", "/bank/create").permitAll()
                        .requestMatchers("/bank/home/**").hasRole("CUSTOMER")
                        .requestMatchers("/bank/admin/**").hasRole("MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/bank/login")
                        .loginProcessingUrl("/bank/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/bank/logout")
                        .logoutSuccessUrl("/bank/login?logout")
                        .permitAll()
                );

        return http.build();
    }

// Bcrypt the user entered password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
