package com.timesheet.service;

import com.timesheet.dto.LoginRequest;
import com.timesheet.dto.RegisterRequest;
import com.timesheet.entity.User;
import com.timesheet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public User register(RegisterRequest req) {
        logger.info("register() called");
        logger.debug("register() params: {}", req);

        if (req.getEmail() == null || req.getEmail().isEmpty() ||
                req.getPassword() == null || req.getPassword().isEmpty()) {
            logger.warn("register() missing email or password");
            throw new IllegalArgumentException("Email and password are required");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            logger.warn("register() email already exists: {}", req.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setMobile(req.getMobile());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        User saved = userRepository.save(user);
        logger.info("register() completed for {}", saved.getEmail());
        return saved;
    }

    public User login(LoginRequest req) {
        logger.info("login() called");
        logger.debug("login() params: {}", req);

        if (req.getEmail() == null || req.getEmail().isEmpty() ||
                req.getPassword() == null || req.getPassword().isEmpty()) {
            logger.warn("login() missing email or password");
            throw new IllegalArgumentException("Email and password are required");
        }

        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
        if (!userOpt.isPresent()) {
            logger.warn("login() user not found: {}", req.getEmail());
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            logger.warn("login() bad password for: {}", req.getEmail());
            throw new IllegalArgumentException("Invalid email or password");
        }

        logger.info("login() success for {}", req.getEmail());
        return user;
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
