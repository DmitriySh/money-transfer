package ru.shishmakov.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CustomUserDetailsManager extends InMemoryUserDetailsManager {
    private final UserDetailsService userLoadService;

    @Autowired
    public CustomUserDetailsManager(UserDetailsService userLoadService) {
        super(new ArrayList<>());
        this.userLoadService = userLoadService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userLoadService.loadUserByUsername(username);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
        return userDetails;
    }
}
