package be.kdg.prog6.landside.domain.exception;

public class NoWeighBridgeAvailableException extends LandsideDomainException {
    public NoWeighBridgeAvailableException() {
        super("No WeighBridge is currently available.");
    }
}