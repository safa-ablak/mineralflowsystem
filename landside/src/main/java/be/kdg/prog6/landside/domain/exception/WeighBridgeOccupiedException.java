package be.kdg.prog6.landside.domain.exception;

import be.kdg.prog6.landside.domain.WeighBridgeNumber;

public class WeighBridgeOccupiedException extends LandsideDomainException {
    public WeighBridgeOccupiedException(final WeighBridgeNumber number) {
        super("WeighBridge %s is currently occupied.".formatted(number.value()));
    }
}