package be.kdg.prog6.invoicing.port.in.command;

import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.domain.RawMaterialId;

import static java.util.Objects.requireNonNull;

public record UpdateRawMaterialPricingCommand(
    RawMaterialId rawMaterialId,
    Money storagePrice,
    Money unitPrice
) {
    public UpdateRawMaterialPricingCommand {
        requireNonNull(rawMaterialId);
    }
}
