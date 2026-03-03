package be.kdg.prog6.landside.domain;

import java.util.Random;

import static java.lang.String.format;

public record Dock(String number) {
    private static final String DOCK_NR_FORMAT = "D-%02d";

    public static Dock random() {
        final int dockNumber = new Random().nextInt(5) + 1;
        return new Dock(format(DOCK_NR_FORMAT, dockNumber));
    }
}