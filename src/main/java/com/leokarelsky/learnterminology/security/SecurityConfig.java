package com.leokarelsky.learnterminology.security;

import com.leokarelsky.learnterminology.model.User;
import com.leokarelsky.learnterminology.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            Optional<User> user = userRepo.findByEmail(username);
            if (user.isPresent()) {
                return user.get();
            }
            throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers("/users/create").permitAll()
                .requestMatchers("/users/all").hasRole("ADMIN")
                .anyRequest().fullyAuthenticated()

                .and()
                .exceptionHandling().accessDeniedPage("/forbidden")

                .and()
                .formLogin()
                .loginPage("/form-login")
                .usernameParameter("email")
                .loginProcessingUrl("/form-login")
                .defaultSuccessUrl("/")
                .failureUrl("/form-login?error").permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/form-login")

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .build();
    }

}