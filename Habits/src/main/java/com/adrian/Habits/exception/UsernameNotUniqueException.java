package com.adrian.Habits.exception;

public class UsernameNotUniqueException extends RuntimeException {
    public UsernameNotUniqueException(String message){
        super(message);
    }
}
