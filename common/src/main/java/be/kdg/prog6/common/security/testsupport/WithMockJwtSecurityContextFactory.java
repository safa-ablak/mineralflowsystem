package be.kdg.prog6.common.security.testsupport;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.Map;

/**
 * Creates a mock SecurityContext with a fake JWT token for test execution.
 * <p>
 * This is used by the @WithMockJwt annotation to simulate an authenticated user
 * with a given role and email, mimicking how Spring Security + OAuth2 works at runtime.
 * <p>
 * Enables realistic @PreAuthorize tests and avoids hardcoding JWT-parsing logic in tests.
 */
public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {
    @Override
    public SecurityContext createSecurityContext(final WithMockJwt annotation) {
        final String role = annotation.role();
        final Jwt jwt = Jwt.withTokenValue("mock-token")
            .header("alg", "none")
            .subject(annotation.sub())
            .claim("realm_access", Map.of("roles", List.of(role)))
            .claim("email", annotation.email())
            .build();
        final JwtAuthenticationToken auth = new JwtAuthenticationToken(
            jwt,
            List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
