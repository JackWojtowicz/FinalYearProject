package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class securityconfig {

        @Autowired
        private UserService userService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((requests) -> requests
                                                .requestMatchers("/", "/style/**").permitAll()
                                                .requestMatchers("/register", "/registerUser").permitAll()
                                                // .requestMatchers("/user").hasAuthority("ROLE_USER")
                                                .requestMatchers("/admin", "/admin/**")
                                                .hasAuthority("ROLE_MEGAADMIN")
                                                .anyRequest().authenticated())
                                .userDetailsService(userService)
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/goals")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login")
                                                .permitAll())
                                .headers().frameOptions().sameOrigin();

                return http.build();
        }

}
