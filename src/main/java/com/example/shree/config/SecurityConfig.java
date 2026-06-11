package com.example.shree.config;

import com.example.shree.filters.JWTAuthFilter;
import com.example.shree.repository.UserRepo;
import com.example.shree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private  JWTAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterchain(HttpSecurity http, JWTAuthFilter jWTAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**","/auth/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**","/auth/**").permitAll()
                        .requestMatchers("/actuator/**","/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/user/**")
                        .hasAnyRole("ADMIN","USER")
                        .anyRequest()
                        .authenticated())
                .headers(headers -> headers.frameOptions(frame-> frame.disable()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public AuthenticationManager authenticationManager( UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

}
