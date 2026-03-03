package be.kdg.prog6.adapter.in.web.controller.dto;

import be.kdg.prog6.common.security.UserRole;

public record AuthenticatedUserDto(String email,
                                   String firstName,
                                   String lastName,
                                   RoleDto role) {
    public static AuthenticatedUserDto from(final String email,
                                            final String firstName,
                                            final String lastName,
                                            final UserRole role) {
        return new AuthenticatedUserDto(email, firstName, lastName, RoleDto.of(role));
    }
}
