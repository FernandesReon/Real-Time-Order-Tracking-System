package com.reon.order_backend.service;

import com.reon.order_backend.dto.UserResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface AdminService {
    Page<UserResponse> fetchAllUsers(int pageNo, int pageSize);
    UserResponse fetchById(ObjectId id);
    UserResponse fetchByEmail(String email);
}
