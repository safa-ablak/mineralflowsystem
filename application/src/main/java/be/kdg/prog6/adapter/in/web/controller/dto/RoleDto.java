package be.kdg.prog6.adapter.in.web.controller.dto;

import be.kdg.prog6.common.security.UserRole;

public record RoleDto(String code, String displayName) {
    public static RoleDto of(final UserRole role) {
        return new RoleDto(role.name(), role.getDisplayName());
    }
}