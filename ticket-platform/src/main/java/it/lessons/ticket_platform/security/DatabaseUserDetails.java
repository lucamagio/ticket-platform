package it.lessons.ticket_platform.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.lessons.ticket_platform.model.Role;
import it.lessons.ticket_platform.model.User;


public class DatabaseUserDetails implements UserDetails {
    
    private final Integer id;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;


    public DatabaseUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = new ArrayList<>();
        for(Role ruolo : user.getRoles()) {
            this.authorities.add(new SimpleGrantedAuthority(ruolo.getName()));
        }
    
    }

    public Integer getId() {
        return this.id;
    }
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
