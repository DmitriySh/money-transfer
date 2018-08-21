package ru.shishmakov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.shishmakov.dao.UserRepository;
import ru.shishmakov.domain.User;

import java.util.List;
import java.util.Set;

import static ru.shishmakov.security.SecurityConfig.API_PASSWORD;
import static ru.shishmakov.security.SecurityConfig.API_ROLE;
import static ru.shishmakov.security.SecurityConfig.API_USERNAME;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

    @RequiredArgsConstructor
    @Component
    @Slf4j
    static class DefaultDataInitializer implements CommandLineRunner {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) {
            userRepository.saveAll(List.of(
                    User.builder()
                            .username(API_USERNAME)
                            .password(passwordEncoder.encode(API_PASSWORD))
                            .roles(Set.of("ROLE_" + API_ROLE))
                            .build(),
                    User.builder()
                            .username("user")
                            .password(this.passwordEncoder.encode("password"))
                            .roles(Set.of("ROLE_USER"))
                            .build(),
                    User.builder()
                            .username("admin")
                            .password(this.passwordEncoder.encode("password"))
                            .roles(Set.of("ROLE_USER", "ROLE_ADMIN"))
                            .build()));

            log.debug("printing all test users...");
            userRepository.findAll().forEach(u -> log.debug(" User :" + u.toString()));
        }
    }
}
