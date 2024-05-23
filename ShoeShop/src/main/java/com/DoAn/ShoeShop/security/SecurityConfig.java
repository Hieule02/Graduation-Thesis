package com.DoAn.ShoeShop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf-> csrf.disable());
        http.authorizeHttpRequests(auth ->{
            auth.requestMatchers( "/users/register", "/login", "/images/**",
                            "/logout", "/", "/products/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
        });
        http.formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/"));
        http.rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret"));
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID"));
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
