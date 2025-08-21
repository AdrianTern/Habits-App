package com.adrian.Habits.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import com.adrian.Habits.utils.Constants;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final Pattern pattern = Pattern.compile(Constants.VALIDATION_PASSWORD_PATTERN);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && pattern.matcher(password).matches();
    }
}
