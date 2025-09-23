package com.adrian.Habits.jwt.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfiguration {
    private String secret;
}
