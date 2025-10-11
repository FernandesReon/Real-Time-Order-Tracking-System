package com.reon.order_backend.controller;

import com.reon.order_backend.dto.UserResponse;
import com.reon.order_backend.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "endpoints only accessible for user with admin role", path = "/api/v1/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(
            name = "endpoint to fetch all users",
            path = "/users")
    public ResponseEntity<Page<UserResponse>> fetchUsers(@RequestParam(name = "page", defaultValue = "0") int pageNo,
                                                         @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        log.info("Admin Controller :: Incoming request for fetching user from page: {} of size: {}", pageNo, pageSize);
        Page<UserResponse> users = adminService.fetchAllUsers(pageNo, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> fetchUserViaId(@PathVariable(name = "id") ObjectId id) {
        log.info("Admin Controller :: Incoming request for fetching user with id: {}", id);
        UserResponse user = adminService.fetchById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> fetchUserViaEmail(@PathVariable(name = "email") String email) {
        log.info("Admin Controller :: Incoming request for fetching user with email: {}", email);
        UserResponse user = adminService.fetchByEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }
}
