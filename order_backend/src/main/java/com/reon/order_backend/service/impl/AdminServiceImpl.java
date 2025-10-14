package com.reon.order_backend.service.impl;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.user.UserResponse;
import com.reon.order_backend.exception.UserNotFoundException;
import com.reon.order_backend.mapper.UserMapper;
import com.reon.order_backend.repository.UserRepository;
import com.reon.order_backend.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponse> fetchAllUsers(int pageNo, int pageSize) {
        log.info("Admin Service :: Fetching users from page: {} of size: {}", pageNo, pageSize);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserMapper::responseToUser);
    }

    @Override
    public UserResponse fetchById(ObjectId id) {
        log.info("Admin Service :: Fetching user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with provided details.")
        );
        log.info("Admin Service :: Fetched user with id: {}", id);
        return UserMapper.responseToUser(user);
    }

    @Override
    public UserResponse fetchByEmail(String email) {
        log.info("Admin Service :: Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found with provided details.")
        );
        log.info("Admin Service :: Fetched user with email: {}", email);
        return UserMapper.responseToUser(user);
    }
}
