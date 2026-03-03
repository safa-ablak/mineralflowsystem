package be.kdg.prog6.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Provides a UTC {@link Clock} bean for consistent timestamp handling.
 * Inject the Clock to enable testability with fixed time.
 */
@Configuration
public class TimeConfig {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}