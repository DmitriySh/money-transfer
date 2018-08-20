package ru.shishmakov.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// TODO: 20.08.2018 delete
public class BasicConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private BasicTokenProvider basicTokenProvider;

    public BasicConfigurerAdapter(BasicTokenProvider basicTokenProvider) {
        this.basicTokenProvider = basicTokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) {
        BasicTokenFilter customFilter = new BasicTokenFilter(basicTokenProvider);
        http.addFilterAfter(customFilter, BasicAuthenticationFilter.class);
    }
}
