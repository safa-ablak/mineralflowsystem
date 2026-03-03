package be.kdg.prog6.common;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum BoundedContext {
    LANDSIDE("Landside BC"),
    WAREHOUSING("Warehousing BC"),
    WATERSIDE("Waterside BC"),
    INVOICING("Invoicing BC");

    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    BoundedContext(final String displayName) {
        this.displayName = displayName;
    }

    public static String toFormattedList() {
        return Arrays.stream(values())
            .map(bc -> "- " + bc.displayName)
            .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return displayName;
    }
}
