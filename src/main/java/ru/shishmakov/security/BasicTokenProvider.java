package ru.shishmakov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
// TODO: 20.08.2018 delete
public class BasicTokenProvider {
    private static final String AUTHORIZATION_TOKEN = "Authorization";
    private static final String WEB_API_USERNAME = "api";
    private static final String WEB_API_PASSWORD = "pass";

    private final UserDetailsService userLoadService;
    private Pattern delimiter = Pattern.compile(":");

    public String resolveToken(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(AUTHORIZATION_TOKEN))
                .filter(h -> h.startsWith("Basic "))
                .map(h -> h.substring(6))
                .map(Base64.getDecoder()::decode)
                .map(ar -> new String(ar, StandardCharsets.UTF_8))
                .map(String::trim)
                .orElse(null);
    }

    public boolean validateToken(String token) {
        String[] loginPass = delimiter.split(token);
        return Objects.equals(loginPass[0], WEB_API_USERNAME)
                && Objects.equals(loginPass[1], WEB_API_PASSWORD);
    }

    public Authentication getAuthentication(String token) {
        // TODO: 20.08.2018
        return null;
    }
}
