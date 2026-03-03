package be.kdg.prog6.landside.domain;

public record WeighBridgeNumber(String value) {
    private static final String WB_NR_FORMAT = "WB-%02d";

    public static WeighBridgeNumber of(final int number) {
        return new WeighBridgeNumber(String.format(WB_NR_FORMAT, number));
    }

    public static WeighBridgeNumber of(final String number) {
        return new WeighBridgeNumber(number);
    }
}