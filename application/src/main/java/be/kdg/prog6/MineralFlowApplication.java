package be.kdg.prog6;

import be.kdg.prog6.common.annotation.BoundedContextMessagingConfig;
import be.kdg.prog6.common.config.TimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

/**
 * Main application entry point for the application module.
 * This class is responsible for launching the Spring Boot application
 * with customized component scanning to avoid loading certain configurations.
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(excludeFilters = {
    // Exclude other @SpringBootApplication classes from other modules (e.g. LandsideApplication)
    @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = SpringBootApplication.class),
    // Avoid loading per-BC messaging configs by custom annotation
    @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = BoundedContextMessagingConfig.class)
})
@Import(TimeConfig.class)
public class MineralFlowApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(MineralFlowApplication.class);

    public static void main(final String[] args) {
        /* Safety net: forces UTC regardless of host machine timezone, ensuring LocalDateTime.now() always returns UTC. */
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        LOGGER.info("Timezone set to: {}", TimeZone.getDefault().getID());
        SpringApplication.run(MineralFlowApplication.class, args);
    }
}