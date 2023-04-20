package com.example.myLibrary.security;


import com.example.myLibrary.model.Login;
import com.example.myLibrary.repository.LoginRepository;
import com.example.myLibrary.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private LoginRepository userRepository;

    public CustomUserDetailsService(LoginRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username don't found."));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}