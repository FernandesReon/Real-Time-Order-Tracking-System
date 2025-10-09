package com.reon.order_backend.mapper;

import com.reon.order_backend.document.User;
import com.reon.order_backend.dto.UserRequest;
import com.reon.order_backend.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static User mapToEntity(UserRequest dto) {
        /*
        similar to:
            User user = new User();
            user.setName(dto.getName());
            return user;
         */
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public static UserResponse responseToUser(User user) {
        /*
        similar to:
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            return response;
         */
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .accountEnabled(user.isAccountEnabled())
                .roles(user.getRoles())
                .createdOn(user.getCreatedOn())
                .updatedOn(user.getUpdatedOn())
                .orderList(user.getOrderList())
                .build();
    }
}
