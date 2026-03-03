package be.kdg.prog6.config.security;

import be.kdg.prog6.common.security.UserRole;
import be.kdg.prog6.common.security.UserRoleUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    // We use SessionCreationPolicy.STATELESS because our backend is an OAuth2 Resource Server.
    // It doesn't manage login sessions or store authentication state - each request must carry a valid JWT.
    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(authorize ->
                authorize.anyRequest().authenticated()
            )
            .sessionManagement(mgmt ->
                mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2ResourceServer(rs ->
                rs.jwt(jwt -> jwtAuthenticationConverter())
            )
            .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        /* In a production environment, these values would typically be externalized to application.yml */
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow requests only from the React frontend during development
        configuration.setAllowedMethods(List.of("GET", "POST")); // Only allow GET and POST API methods used for the frontend
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allow headers needed for sending JWTs and JSON payloads
        configuration.setAllowCredentials(true); // Allow cookies and Authorization headers to be sent with cross-origin requests
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            final UserRole userRole = UserRoleUtil.extractRole(jwt.getClaims());
            return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name())); // Convert to Spring Security format
        });
        return converter;
    }
}
