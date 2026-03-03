package be.kdg.prog6.adapter.in.web.controller.dto;

import be.kdg.prog6.common.BoundedContext;

public record BoundedContextDto(String code, String displayName) {
    public static BoundedContextDto of(final BoundedContext boundedContext) {
        return new BoundedContextDto(boundedContext.name(), boundedContext.getDisplayName());
    }
}
