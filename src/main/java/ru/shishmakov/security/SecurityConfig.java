package ru.shishmakov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static ru.shishmakov.security.CustomMemoryUserDetailsManager.WEB_API_PASSWORD;
import static ru.shishmakov.security.CustomMemoryUserDetailsManager.WEB_API_ROLE;
import static ru.shishmakov.security.CustomMemoryUserDetailsManager.WEB_API_USERNAME;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomMemoryUserDetailsManager detailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.apply(new CustomInMemoryUserDetailsManagerConfigurer<>(detailsService))
                .withUser(WEB_API_USERNAME)
                .password(WEB_API_PASSWORD)
                .roles(WEB_API_ROLE);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/auth/signin").hasRole("API")
                .antMatchers("/auth/decode").permitAll()
                .antMatchers(HttpMethod.GET, "/api").permitAll() // don't need a token
                .antMatchers(HttpMethod.PUT, "/api/accounts/transfer", "/api/account/**").hasRole("ADMIN") // need token
//                .antMatchers(HttpMethod.PUT, "/api/accounts/transfer", "/api/account/**").access("hasRole('ROLE_API') or hasRole('ROLE_ADMIN')") // need token
                .anyRequest().authenticated()

                .and()
                .apply(new JwtConfigurerAdapter(jwtTokenProvider));
//                .and()
//                .apply(new BasicConfigurerAdapter(basicTokenProvider));
    }

    private static class CustomInMemoryUserDetailsManagerConfigurer<B extends ProviderManagerBuilder<B>>
            extends UserDetailsManagerConfigurer<B, CustomInMemoryUserDetailsManagerConfigurer<B>> {
        CustomInMemoryUserDetailsManagerConfigurer(CustomMemoryUserDetailsManager detailsService) {
            super(detailsService);
        }
    }
}
