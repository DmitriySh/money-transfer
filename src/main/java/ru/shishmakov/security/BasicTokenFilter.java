package ru.shishmakov.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// TODO: 20.08.2018 delete
public class BasicTokenFilter extends GenericFilterBean {
    private final BasicTokenProvider basicTokenProvider;

    public BasicTokenFilter(BasicTokenProvider basicTokenProvider) {
        this.basicTokenProvider = basicTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        String token = basicTokenProvider.resolveToken((HttpServletRequest) req);
        if (token != null && basicTokenProvider.validateToken(token)) {
            Authentication auth = basicTokenProvider.getAuthentication(token);
            if (auth != null) SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(req, res);
    }
}
