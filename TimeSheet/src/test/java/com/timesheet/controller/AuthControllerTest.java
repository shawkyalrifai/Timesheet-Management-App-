package com.timesheet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheet.dto.LoginRequest;
import com.timesheet.dto.RegisterRequest;
import com.timesheet.entity.User;
import com.timesheet.service.UserService;
import com.timesheet.utils.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("shawky");
        user.setEmail("shawky@gmail.com");
        user.setMobile("01129446553");
    }

    @Test
    public void register_ShouldReturnSuccess() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setName("shawky");
        req.setEmail("shawky@gmail.com");
        req.setPassword("123");
        req.setMobile("01129446553");

        Mockito.when(userService.register(any(RegisterRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.user.email").value("shawky@gmail.com"));
    }
    @Test
    public void login_ShouldReturnToken() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("shawky@gmail.com");
        req.setPassword("password123");

        Mockito.when(userService.login(any(LoginRequest.class))).thenReturn(user);
        Mockito.when(jwtUtil.generateToken(eq("john@example.com"))).thenReturn("null");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.user.email").value("shawky@gmail.com"))
                .andExpect(jsonPath("$.token").isEmpty());

    }
    @Test
    public void logout_ShouldReturnSuccess() throws Exception {
        mockMvc.perform((RequestBuilder) post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.message").value("User logged out successfully"));
    }
}