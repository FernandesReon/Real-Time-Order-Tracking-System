package com.reon.order_backend.service.impl;

import com.reon.order_backend.document.User;
import com.reon.order_backend.exception.UserNotFoundException;
import com.reon.order_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("Custom User Detail Service :: loadUserByEmail: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found with provided email.")
        );
        return user;
    }
}
