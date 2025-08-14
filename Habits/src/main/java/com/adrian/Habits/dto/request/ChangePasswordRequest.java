package com.adrian.Habits.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

// Request to change user password
@Data
@Builder
public class ChangePasswordRequest {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String newPassword;
    
}
