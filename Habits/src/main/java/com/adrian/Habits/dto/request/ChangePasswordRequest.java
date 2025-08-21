package com.adrian.Habits.dto.request;

import com.adrian.Habits.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

// Request to change user password
@Data
@Builder
public class ChangePasswordRequest {
    @NotBlank
    private String oldPassword;

    @ValidPassword
    private String newPassword;
    
}
