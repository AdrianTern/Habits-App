package com.adrian.Habits.configuration;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class FixedClockConfig {
    @Bean
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(LocalDate.of(2025, 5, 5)
                .atStartOfDay(ZoneId.of("Asia/Kuala_Lumpur"))
                .toInstant(),
                ZoneId.of("Asia/Kuala_Lumpur"));
    }
}
