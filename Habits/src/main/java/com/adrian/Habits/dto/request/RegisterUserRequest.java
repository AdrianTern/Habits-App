package com.adrian.Habits.dto.request;

import com.adrian.Habits.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Request to create an user
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    
    @NotBlank
    private String username;

    @ValidPassword
    private String password;
}
