package com.reon.order_backend.service.impl;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.user.UserLogin;
import com.reon.order_backend.dto.user.UserRequest;
import com.reon.order_backend.dto.user.UserResponse;
import com.reon.order_backend.exception.EmailAlreadyExistsException;
import com.reon.order_backend.jwt.JwtResponse;
import com.reon.order_backend.jwt.JwtUtils;
import com.reon.order_backend.mapper.UserMapper;
import com.reon.order_backend.repository.UserRepository;
import com.reon.order_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserResponse registration(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("User with this email already exists.");
        }
        log.info("User Service :: New Registration in progress....");
        User user = UserMapper.mapToEntity(request);

        log.info("User Service :: Encoding the password using BCrypt...");
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        log.info("User Service :: Default role is USER");
        user.setRoles(EnumSet.of(User.Role.USER));

        log.info("User Service :: Enabling the account...");
        user.setAccountEnabled(true);

        User savedUser = userRepository.save(user);
        log.info("User Service :: User saved: {}", request);
        return UserMapper.responseToUser(savedUser);
    }

    @Override
    public JwtResponse authenticateUser(UserLogin login) {
        log.info("User Service :: Authenticating User: {}", login);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateToken((User) userDetails);
        return new JwtResponse(jwtToken);
    }
}
