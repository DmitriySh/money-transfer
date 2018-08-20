package ru.shishmakov.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CustomMemoryUserDetailsManager extends InMemoryUserDetailsManager implements UserDetailsManager {
    public static final String WEB_API_USERNAME = "api";
    public static final String WEB_API_PASSWORD = "password";
    public static final String WEB_API_ROLE = "API";

    private final UserDetailsService userLoadService;

    @Autowired
    public CustomMemoryUserDetailsManager(UserDetailsService userLoadService) {
        super(new ArrayList<>());
        this.userLoadService = userLoadService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userLoadService.loadUserByUsername(username);
    }
}
