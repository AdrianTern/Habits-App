package com.adrian.Habits.utils;

import com.adrian.Habits.model.UserEntity;

//Builder class to create UserEntity
public class MockUserBuilder {
    private String username = "admin";
    private String password = "admin123";

    public MockUserBuilder withUsername(String username){
        this.username = username;
        return this;
    }

    public MockUserBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public UserEntity build(){
        return UserEntity.builder()
                        .username(username)
                        .password(password)
                        .build();
    }
}
