package be.kdg.prog6.common.security.testsupport;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

/**
 * Test annotation to simulate an authenticated user with a JWT.
 * <p>
 * Useful for integration and controller tests where security roles and claims
 * (like role, email, or subject/user ID) need to be mocked without relying on real tokens or Keycloak.
 * <p>
 * The {@code sub} attribute sets the JWT subject claim, which corresponds to the Keycloak user ID.
 * Controllers that extract the authenticated user's identity via {@code jwt.getSubject()} will
 * receive this value.
 * <p>
 * Example:
 *
 * @WithMockJwt(role = "SELLER", sub = "23f11460-22bb-4888-b52a-2c5f6c9d1ea5", email = "seller@email.com")
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwt {
    String role() default "UNKNOWN";

    String email() default "test@example.com";

    String sub() default "00000000-0000-0000-0000-000000000000";
}
