package ru.shishmakov.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.shishmakov.dao.UserRepository;
import ru.shishmakov.domain.User;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class CustomUserDetailsManager extends InMemoryUserDetailsManager {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsManager(UserRepository userRepository) {
        super(new ArrayList<>());
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        return byUsername
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }
}
