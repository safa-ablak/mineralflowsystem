package be.kdg.prog6.waterside;

import be.kdg.prog6.common.config.TimeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TimeConfig.class)
public class WatersideApplication {
    public static void main(final String[] args) {
        SpringApplication.run(WatersideApplication.class, args);
    }
}
