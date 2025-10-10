package com.reon.order_backend.service;

import com.reon.order_backend.dto.UserLogin;
import com.reon.order_backend.dto.UserRequest;
import com.reon.order_backend.dto.UserResponse;
import com.reon.order_backend.jwt.JwtResponse;

public interface UserService {
    UserResponse registration(UserRequest request);
    JwtResponse authenticateUser(UserLogin login);
}
