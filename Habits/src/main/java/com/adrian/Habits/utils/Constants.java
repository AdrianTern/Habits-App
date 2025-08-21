package com.adrian.Habits.utils;

public final class Constants {

    private Constants() {}

    // Endpoints
    // Auth
    public static final String ENDPOINT_AUTH_BASE = "/api/auth";
    public static final String ENDPOINT_AUTH_REGISTER = "/register";
    public static final String ENDPOINT_AUTH_REGISTER_FULL = "/api/auth/register";
    public static final String ENDPOINT_AUTH_LOGIN = "/login";
    public static final String ENDPOINT_AUTH_LOGIN_FULL = "/api/auth/login";
    public static final String ENDPOINT_AUTH_CHANGE_PASSWORD = "/changePassword/{id}";
    public static final String ENDPOINT_AUTH_CHANGE_PASSWORD_FULL = "/api/auth/changePassword/{id}";
    // Task
    public static final String ENDPOINT_TASK_BASE = "/api/tasks";
    public static final String ENDPOINT_TASK_TASKCOUNT = "/taskCount";
    public static final String ENDPOINT_TASK_TASKCOUNT_FULL = "/api/taskCount";
    // User
    public static final String ENDPOINT_USER_BASE = "/api/users";

    // AppConfig key
    public static final String CONFIG_KEY_CLEANUP = "cleanup_config";
    public static final String CONFIG_KEY_RESET_ROUTINE = "resetRoutine_config";

    // Exception feedback message
    public static final String EXCEPTION_USER_NOT_FOUND = "User not found";
    public static final String EXCEPTION_TASK_NOT_FOUND = "Task not found";
    public static final String EXCEPTION_USERNAME_TAKEN = "The username is already taken";
    public static final String EXCEPTION_INCORRECT_OLD_PASSWORD = "Old password is incorrect";
    public static final String EXCEPTION_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String EXCEPTION_INVALID_TITLE = "Title cannot be blank";
    public static final String EXCEPTION_INVALID_PASSWORD_PATTERN = "Password must be at least 8 characters, "
            + "include one uppercase letter, one lowercase letter, "
            + "one number, and one special character";

    // Json property
    public static final String JSON_ROUTINE_DETAILS = "routineDetailsResponse";

    // Validation
    public static final String VALIDATION_PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
}
