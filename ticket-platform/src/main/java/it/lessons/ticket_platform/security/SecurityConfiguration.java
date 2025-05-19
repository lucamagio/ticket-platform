package it.lessons.ticket_platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private MyAuthenticationSuccessHandler successHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers("/admin", "/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/ticket/createTicket", "/ticket/editTicket/**").hasAuthority("ADMIN")
                .requestMatchers("/user", "/user/**").hasAuthority("OPERATORE")
                .requestMatchers(HttpMethod.POST, "/ticket/editTicketStatus/**").hasAuthority("OPERATORE")
                .requestMatchers("/ticket/dettaglioTicket/**", "/note/**").hasAnyAuthority("OPERATORE", "ADMIN")
                .requestMatchers("/**").permitAll()
                .and().formLogin()
                .and().logout()
                .and().exceptionHandling()
                .and().csrf().disable();

        return http.build();
    }

    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
