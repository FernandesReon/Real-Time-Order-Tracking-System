package com.reon.order_backend.controller;

import com.reon.order_backend.dto.user.UserLogin;
import com.reon.order_backend.dto.user.UserRequest;
import com.reon.order_backend.dto.user.UserResponse;
import com.reon.order_backend.jwt.JwtResponse;
import com.reon.order_backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "endpoints related to user operations.", path = "/api/v1/auth")
@Slf4j
public class AuthController {
    private final UserService userService;

    @Value("${jwt.expiration-time}")
    private Long tokenExpirationTime;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            name = "endpoint for registering new user",
            path = "/sign-up"
    )
    public ResponseEntity<UserResponse> userRegistration(@Valid @RequestBody UserRequest dto) {
        log.info("Auth Controller :: Incoming request for registration: {}", dto.getEmail());
        UserResponse response =  userService.registration(dto);
        log.info("Auth Controller :: User Registration successful.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            name = "endpoint for authenticating user",
            path = "/sign-in"
    )
    public ResponseEntity<JwtResponse> userAuthentication(@Valid @RequestBody UserLogin login,
                                                          HttpServletResponse response) {
        log.info("Auth Controller :: Incoming login request: {}", login);
        JwtResponse jwtResponse = userService.authenticateUser(login);

        log.info("Auth Controller :: Saving the jwt token to cookie.");
        Cookie cookie = new Cookie("JWT", jwtResponse.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(false);          // true during production
        cookie.setSecure(true);
        cookie.setMaxAge((int) (tokenExpirationTime / 1000));
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
        log.info("Auth Controller :: Saved the cookie to Cookie");

        log.info("Auth Controller :: Authentication successful: {}", jwtResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtResponse);
    }

    @PostMapping(
            name = "endpoint for logging out",
            path = "/sign-out"
    )
    public ResponseEntity<?> logout(HttpServletResponse response) {
        log.info("Auth Controller :: Incoming request for logging out.");

        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);

        log.info("Auth Controller :: Logout successful.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
