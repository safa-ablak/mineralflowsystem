package be.kdg.prog6.landside.adapter.config;

import be.kdg.prog6.common.annotation.BoundedContextMessagingConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@BoundedContextMessagingConfig
public class LandsideMessagingConfig {
    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, final ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter(final ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }
}
