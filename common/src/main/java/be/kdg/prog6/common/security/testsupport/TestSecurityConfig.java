package be.kdg.prog6.common.security.testsupport;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Minimal security configuration for bounded context integration tests.
 * <p>
 * Replaces the production SecurityConfig to decouple BC tests from
 * CORS, OAuth2 resource server, and Keycloak dependencies.
 * <p>
 * Works with {@link WithMockJwt} to test {@code @PreAuthorize} authorization rules,
 * and returns 401 for unauthenticated requests.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class TestSecurityConfig {
    @Bean
    SecurityFilterChain testFilterChain(final HttpSecurity http) throws Exception {
        return http
            /* Stateless JWT auth – no session cookies to forge */
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize ->
                authorize.anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpStatus.UNAUTHORIZED.value())
                )
            )
            .build();
    }
}
