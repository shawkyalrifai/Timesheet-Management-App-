package com.timesheet.controller;

import com.timesheet.dto.LoginRequest;
import com.timesheet.dto.RegisterRequest;
import com.timesheet.entity.User;
import com.timesheet.service.UserService;
import com.timesheet.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger logger =
            LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        logger.info("AuthController.register called");
        logger.debug("params: {}", req);

        User user = userService.register(req);
        user.setPassword(null);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("user", user);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        logger.info("AuthController.login called");
        logger.debug("params: {}", req);

        User user = userService.login(req);
        user.setPassword(null);

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("user", user);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        logger.info("AuthController.logout called");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User logged out successfully");

        return ResponseEntity.ok(response);
    }
}
