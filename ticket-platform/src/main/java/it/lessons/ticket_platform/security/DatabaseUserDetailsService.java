package it.lessons.ticket_platform.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.lessons.ticket_platform.model.User;
import it.lessons.ticket_platform.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    public DatabaseUserDetailsService() {
    }

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optUser = userRepository.findByEmail(email);

        if(optUser.isPresent()) {
            return new DatabaseUserDetails(optUser.get());
        } else {
            throw new UsernameNotFoundException("Email non trovata");
        }
    }


}
