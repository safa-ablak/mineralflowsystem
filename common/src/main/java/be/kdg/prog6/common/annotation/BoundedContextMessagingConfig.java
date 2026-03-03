package be.kdg.prog6.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks a messaging config or topology class as belonging to a specific bounded context.
 *
 * <p>In the Mineral Flow System, this annotation is used to filter out messaging configurations
 * when running the full application module that integrates all bounded contexts.
 *
 * <p>It has no effect when running a single bounded context as a standalone module,
 * but it documents the config as context-specific and prevents unintended loading in the full app.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface BoundedContextMessagingConfig {
}