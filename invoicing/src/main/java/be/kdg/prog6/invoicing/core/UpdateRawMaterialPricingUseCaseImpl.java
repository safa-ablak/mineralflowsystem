package be.kdg.prog6.invoicing.core;

import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.domain.RawMaterialId;
import be.kdg.prog6.invoicing.domain.exception.RawMaterialNotFoundException;
import be.kdg.prog6.invoicing.port.in.command.UpdateRawMaterialPricingCommand;
import be.kdg.prog6.invoicing.port.in.usecase.UpdateRawMaterialPricingUseCase;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;
import be.kdg.prog6.invoicing.port.out.UpdateRawMaterialPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateRawMaterialPricingUseCaseImpl implements UpdateRawMaterialPricingUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateRawMaterialPricingUseCaseImpl.class);

    private final LoadRawMaterialPort loadRawMaterialPort;
    private final UpdateRawMaterialPort updateRawMaterialPort;

    public UpdateRawMaterialPricingUseCaseImpl(final LoadRawMaterialPort loadRawMaterialPort,
                                               final UpdateRawMaterialPort updateRawMaterialPort) {
        this.loadRawMaterialPort = loadRawMaterialPort;
        this.updateRawMaterialPort = updateRawMaterialPort;
    }

    @Override
    @Transactional
    public RawMaterial updateRawMaterialPricing(final UpdateRawMaterialPricingCommand command) {
        final RawMaterialId id = command.rawMaterialId();
        final Money storagePrice = command.storagePrice();
        final Money unitPrice = command.unitPrice();

        LOGGER.info("Updating pricing for Raw Material with ID {}", id.id());

        final RawMaterial rawMaterial = loadRawMaterialPort.loadRawMaterialById(id).orElseThrow(
            () -> RawMaterialNotFoundException.forId(id)
        );
        final String name = rawMaterial.getName();
        if (storagePrice != null) {
            final Money oldStoragePrice = rawMaterial.getStoragePricePerTonPerDay();
            rawMaterial.modifyStoragePricePerTonPerDay(storagePrice);
            final Money newStoragePrice = rawMaterial.getStoragePricePerTonPerDay();
            LOGGER.info("Updated storage price for Raw Material {} from {} to {}", name, oldStoragePrice, newStoragePrice);
        }
        if (unitPrice != null) {
            final Money oldUnitPrice = rawMaterial.getUnitPricePerTon();
            rawMaterial.modifyUnitPricePerTon(unitPrice);
            final Money newUnitPrice = rawMaterial.getUnitPricePerTon();
            LOGGER.info("Updated unit price for Raw Material {} from {} to {}", name, oldUnitPrice, newUnitPrice);
        }
        updateRawMaterialPort.updateRawMaterial(rawMaterial);
        return rawMaterial;
    }
}
