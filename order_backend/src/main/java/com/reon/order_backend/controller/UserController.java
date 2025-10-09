package com.reon.order_backend.controller;

import com.reon.order_backend.dto.UserRequest;
import com.reon.order_backend.dto.UserResponse;
import com.reon.order_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "endpoints related to user operations.", path = "/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> userRegistration(@Valid @RequestBody UserRequest dto) {
        log.info("User Controller :: Incoming request for registration: {}", dto.getEmail());
        UserResponse response =  userService.registration(dto);
        log.info("User Controller :: User Registration successful.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
