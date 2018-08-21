package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shishmakov.web.AuthenticationRequest;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AccountService accountService;

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signin(@RequestBody AuthenticationRequest data) {
        String token = accountService.createToken(data.getUsername(), data.getPassword());
        return ResponseEntity.ok(Map.of("username", data.getUsername(), "token", token));
    }

    @GetMapping("/decode")
    public ResponseEntity<Map<String, Object>> currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // TODO: 21.08.2018 fix me for both 'Authorization: Basic|Bearer'
        Map<String, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList()));
        return ResponseEntity.ok(model);
    }
}
