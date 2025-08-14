package com.adrian.Habits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
