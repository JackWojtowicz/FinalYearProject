package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.model.SecuredUser;
import com.example.demo.model.User;
import com.example.demo.repositary.UserRepo;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo
                .findByUsername(username)
                .map(SecuredUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails loadUserByEmail(String email) {
        return userRepo
                .findByEmail(email)
                .map(SecuredUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public void saveUser(UserDTO userDTO) {
        userRepo.save(new User(userDTO.getUsername(), userDTO.getEmail(),
                passwordEncoder().encode(userDTO.getPassword()), "ROLE_USER"));
    }
}
