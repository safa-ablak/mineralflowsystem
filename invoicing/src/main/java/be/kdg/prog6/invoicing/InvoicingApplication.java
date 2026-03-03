package be.kdg.prog6.invoicing;

import be.kdg.prog6.common.config.TimeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TimeConfig.class)
public class InvoicingApplication {
    public static void main(final String[] args) {
        SpringApplication.run(InvoicingApplication.class, args);
    }
}
