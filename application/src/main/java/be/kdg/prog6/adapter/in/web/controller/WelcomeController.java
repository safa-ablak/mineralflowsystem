package be.kdg.prog6.adapter.in.web.controller;

import be.kdg.prog6.adapter.in.web.controller.dto.AuthenticatedUserDto;
import be.kdg.prog6.adapter.in.web.controller.dto.BoundedContextDto;
import be.kdg.prog6.adapter.in.web.controller.dto.RoleDto;
import be.kdg.prog6.common.BoundedContext;
import be.kdg.prog6.common.ProjectInfo;
import be.kdg.prog6.common.security.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.SYSTEM;
import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static be.kdg.prog6.common.security.UserRoleUtil.extractRole;
import static java.lang.String.format;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeController.class);

    /**
     * Returns the currently authenticated user info (ID, email, first name, last name, and role).
     */
    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserDto> me(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing their authenticated user information");
        final String id = jwt.getSubject();
        final String email = jwt.getClaimAsString("email");
        final String firstName = jwt.getClaimAsString("given_name");
        final String lastName = jwt.getClaimAsString("family_name");
        final UserRole role = extractRole(jwt);
        return ResponseEntity.ok(AuthenticatedUserDto.from(id, email, firstName, lastName, role));
    }

    /**
     * Welcomes the authenticated user and shows available bounded contexts within the system.<br>
     * Only accessible to authenticated users with a "real" role (excluding {@code ROLE_UNKNOWN}).
     */
    @GetMapping("/bounded-contexts")
    @PreAuthorize("!hasRole('ROLE_UNKNOWN')")
    public ResponseEntity<List<BoundedContextDto>> boundedContexts(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format(
            "is viewing available Bounded Contexts within %s%n%s",
            SYSTEM,
            BoundedContext.toFormattedList()
        ));
        final List<BoundedContextDto> boundedContextDtos = Arrays.stream(BoundedContext.values())
            .map(BoundedContextDto::of)
            .toList();
        return ResponseEntity.ok(boundedContextDtos);
    }

    /**
     * Returns all roles available within the system.<br>
     * Only accessible to authenticated users with the {@code ROLE_ADMIN} role.
     */
    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<RoleDto>> roles(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format(
            "is viewing available roles within %s",
            SYSTEM
        ));
        final List<RoleDto> roleDtos = Arrays.stream(UserRole.values())
            .map(RoleDto::of)
            .toList();
        return ResponseEntity.ok(roleDtos);
    }

    /**
     * Returns copyright information about the Mineral Flow system.
     */
    @GetMapping("/copyright")
    public ResponseEntity<String> copyright() {
        return ResponseEntity.ok(ProjectInfo.COPYRIGHT);
    }
}