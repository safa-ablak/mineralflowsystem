package be.kdg.prog6.common.config.testsupport;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Fixed {@link Clock} for controller integration tests where Spring wires the components.
 * <p>
 * Unlike unit tests (where you pass a Clock directly via constructor),
 * integration tests have no control over how Spring injects dependencies.
 * This config overrides the production clock from {@link be.kdg.prog6.common.config.TimeConfig}
 * so that all Spring-managed components receive a deterministic time.
 * <p>
 * {@code @Primary} ensures this clock takes precedence over the production one
 * when both beans are present in the context.
 * <p>
 * Not annotated with {@code @Configuration} to prevent component scanning from
 * picking it up in production. Tests import it explicitly via {@code @Import(TestTimeConfig.class)}.
 */
public class TestTimeConfig {
    public static final LocalDateTime
        FIXED_NOW = LocalDateTime.of(2026, 1, 1, 12, 0);

    @Bean
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(FIXED_NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
