package com.adrian.Habits.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.adrian.Habits.dto.request.CreateUserRequest;
import com.adrian.Habits.dto.response.UserResponse;
import com.adrian.Habits.model.UserEntity;

// Utility mapper class to convert between entity and response
public class UserMapper {

    public static UserResponse toUserResponse(UserEntity user) {
        return UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .build();
    }

    public static UserEntity toUserEntity(CreateUserRequest request, PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build();
    }

}
