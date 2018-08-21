package ru.shishmakov.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication currentAuth = jwtTokenProvider.getAuthentication(token);
            if (currentAuth != null) {
                SecurityContext context = SecurityContextHolder.getContext();
                if (context.getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                    combineAuthenticationRules(currentAuth, context);
                } else context.setAuthentication(currentAuth);
            }
        }
        filterChain.doFilter(req, res);
    }

    private void combineAuthenticationRules(Authentication currentAuth, SecurityContext context) {
        Authentication previousAuth = context.getAuthentication();
        List<GrantedAuthority> combinedRules = Stream.of(currentAuth.getAuthorities(), previousAuth.getAuthorities())
                .flatMap(Collection::stream)
                .collect(toList());
        context.setAuthentication(new UsernamePasswordAuthenticationToken(currentAuth.getPrincipal(), "", combinedRules));
    }
}
