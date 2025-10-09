package com.reon.order_backend.service.impl;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.UserRequest;
import com.reon.order_backend.dto.UserResponse;
import com.reon.order_backend.exception.EmailAlreadyExistsException;
import com.reon.order_backend.mapper.UserMapper;
import com.reon.order_backend.repository.UserRepository;
import com.reon.order_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse registration(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("User with this email already exists.");
        }
        log.info("User Service :: New Registration in progress....");
        User user = UserMapper.mapToEntity(request);
        user.setRoles(EnumSet.of(User.Role.USER));

        User savedUser = userRepository.save(user);
        log.info("User Service :: User saved: {}", request);
        return UserMapper.responseToUser(savedUser);
    }
}
