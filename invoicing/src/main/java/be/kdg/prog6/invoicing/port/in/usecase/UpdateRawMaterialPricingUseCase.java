package be.kdg.prog6.invoicing.port.in.usecase;

import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.port.in.command.UpdateRawMaterialPricingCommand;

@FunctionalInterface
public interface UpdateRawMaterialPricingUseCase {
    RawMaterial updateRawMaterialPricing(UpdateRawMaterialPricingCommand command);
}
