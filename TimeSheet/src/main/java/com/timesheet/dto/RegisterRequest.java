package com.timesheet.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "\\d{11}", message = "Mobile number must be 11 digits")
    private String mobile;

    @NotBlank(message = "Password is required")
    private String password;
}
