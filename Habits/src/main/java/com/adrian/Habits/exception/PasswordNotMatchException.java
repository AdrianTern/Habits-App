package com.adrian.Habits.exception;

public class PasswordNotMatchException extends IllegalArgumentException{
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
