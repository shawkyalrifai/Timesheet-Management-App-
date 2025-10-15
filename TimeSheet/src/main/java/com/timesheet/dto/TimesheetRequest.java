package com.timesheet.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class TimesheetRequest {

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Date is required")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Use yyyy-MM-dd")
        private String date;

        @Pattern(regexp = "^$|^(?:[01]\\d|2[0-3]):[0-5]\\d$",
                message = "Invalid login time format. Use HH:mm")
        private String loginTime;

        @Pattern(regexp = "^$|^(?:[01]\\d|2[0-3]):[0-5]\\d$",
                message = "Invalid logout time format. Use HH:mm")
        private String logoutTime;
    }


