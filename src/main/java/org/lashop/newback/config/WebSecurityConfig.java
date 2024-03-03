package org.lashop.newback.config;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.config.security.TokenFilter;
import org.lashop.newback.config.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private TokenFilter tokenFilter;

    @Autowired
    public void setTokenFilter(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/signUp").permitAll()
                        .requestMatchers("/api/signIn").permitAll()
                        .requestMatchers("/api/home").permitAll()
                        .requestMatchers("/api/deleteAccount").authenticated()
                        .requestMatchers("/api/cart", "/api/cart/**").authenticated()
                        .requestMatchers("/api/categories/{categoryId}").permitAll()
                        .requestMatchers("/api/products/{productId}").permitAll()
                        .requestMatchers("/api/favourites", "/api/favourites/**").authenticated()
                        .requestMatchers("/api/order/**").authenticated()
                        .requestMatchers("/api/address/**").authenticated()
                        .requestMatchers("api/cards/**").authenticated()
                        .requestMatchers("api/adm/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}