package com.reon.order_backend.service;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.UserRequest;
import com.reon.order_backend.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registration(UserRequest request);
}
