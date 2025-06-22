package com.adrian.Habits.dto.response;

import lombok.Builder;
import lombok.Data;

// Response class for User
@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
}
