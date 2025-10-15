package com.timesheet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheet.dto.TimesheetRequest;
import com.timesheet.entity.Timesheet;
import com.timesheet.service.TimesheetService;
import com.timesheet.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TimesheetController.class)
@ContextConfiguration(classes = {TimesheetController.class})
@AutoConfigureMockMvc(addFilters = false)
public class TimesheetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimesheetService timesheetService;

    // Mock JwtUtil to avoid security autowiring errors
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Timesheet mockTimesheet;

    @BeforeEach
    void setUp() {
        mockTimesheet = new Timesheet();
        mockTimesheet.setId(1L);
        mockTimesheet.setDate(LocalDate.now());
        mockTimesheet.setLoginTime(LocalDateTime.now());
    }

    @Test
    void testSubmit() throws Exception {
        TimesheetRequest request = new TimesheetRequest();
        request.setEmail("shawky@gmail.com");
        request.setDate(LocalDate.now().toString());
        request.setLoginTime("08:30");

        Mockito.when(timesheetService.submit(any(TimesheetRequest.class)))
                .thenReturn(mockTimesheet);

        mockMvc.perform(post("/api/timesheet/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.loginTime").exists());
    }

    @Test
    void testToday_withExistingRecord() throws Exception {
        String email = "shawky@gmail.com";
        Mockito.when(timesheetService.getForDay(eq(email), any(LocalDate.class)))
                .thenReturn(Optional.of(mockTimesheet));

        mockMvc.perform(get("/api/timesheet/today")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    void testToday_withNoRecord() throws Exception {
        String email = "asd@example.com";
        Mockito.when(timesheetService.getForDay(eq(email), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/timesheet/today")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No record for today"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    void testDay_withExistingRecord() throws Exception {
        String email = "shawky@gmail.com";
        LocalDate date = LocalDate.of(2025, 10, 15);

        Mockito.when(timesheetService.getForDay(eq(email), eq(date)))
                .thenReturn(Optional.of(mockTimesheet));

        mockMvc.perform(get("/api/timesheet/day")
                        .param("email", email)
                        .param("date", "2025-10-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    void testDay_withNoRecord() throws Exception {
        String email = "empty@example.com";
        LocalDate date = LocalDate.of(2025, 10, 15);

        Mockito.when(timesheetService.getForDay(eq(email), eq(date)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/timesheet/day")
                        .param("email", email)
                        .param("date", "2025-10-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No record for this day"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.date").value("2025-10-15"));
    }
}
