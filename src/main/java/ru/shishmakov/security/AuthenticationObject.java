package ru.shishmakov.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
// TODO: 20.08.2018 delete
public class AuthenticationObject extends UsernamePasswordAuthenticationToken {
    private boolean basicAuth;
    private boolean jwt;

    public AuthenticationObject(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AuthenticationObject(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
