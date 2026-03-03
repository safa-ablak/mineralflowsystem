package be.kdg.prog6.invoicing.adapter.out.db.adapter;

import be.kdg.prog6.invoicing.adapter.out.db.entity.RawMaterialJpaEntity;
import be.kdg.prog6.invoicing.adapter.out.db.repository.RawMaterialJpaRepository;
import be.kdg.prog6.invoicing.adapter.out.db.value.MonetaryValueEmbeddable;
import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.domain.RawMaterialId;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;
import be.kdg.prog6.invoicing.port.out.UpdateRawMaterialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RawMaterialDatabaseAdapter implements LoadRawMaterialPort, UpdateRawMaterialPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(RawMaterialDatabaseAdapter.class);

    private final RawMaterialJpaRepository rawMaterialJpaRepository;

    public RawMaterialDatabaseAdapter(final RawMaterialJpaRepository rawMaterialJpaRepository) {
        this.rawMaterialJpaRepository = rawMaterialJpaRepository;
    }

    @Override
    public Optional<RawMaterial> loadRawMaterialByName(final String name) {
        LOGGER.info("Loading Raw Material with name: {}", name);
        return rawMaterialJpaRepository.findByNameIgnoreCase(name).map(this::toRawMaterial);
    }

    @Override
    public Optional<RawMaterial> loadRawMaterialById(final RawMaterialId id) {
        LOGGER.info("Loading Raw Material with ID: {}", id.id());
        return rawMaterialJpaRepository.findById(id.id()).map(this::toRawMaterial);
    }

    @Override
    public List<RawMaterial> loadRawMaterials() {
        LOGGER.info("Loading all Raw Materials");
        return rawMaterialJpaRepository.findAll().stream().map(this::toRawMaterial).toList();
    }

    @Override
    public void updateRawMaterial(final RawMaterial rawMaterial) {
        final UUID id = rawMaterial.getId().id();
        LOGGER.info("Updating Raw Material with ID {}", id);
        final RawMaterialJpaEntity rawMaterialJpaEntity = rawMaterialJpaRepository.findById(id).orElseThrow();
        final MonetaryValueEmbeddable storagePrice = toMonetaryValueEmbeddable(
            rawMaterial.getStoragePricePerTonPerDay()
        );
        final MonetaryValueEmbeddable unitPrice = toMonetaryValueEmbeddable(
            rawMaterial.getUnitPricePerTon()
        );
        rawMaterialJpaEntity.setStoragePrice(storagePrice);
        rawMaterialJpaEntity.setUnitPrice(unitPrice);
        rawMaterialJpaRepository.save(rawMaterialJpaEntity);
    }

    private RawMaterial toRawMaterial(final RawMaterialJpaEntity rawMaterialJpaEntity) {
        return new RawMaterial(
            RawMaterialId.of(rawMaterialJpaEntity.getId()),
            rawMaterialJpaEntity.getName(),
            toMoney(rawMaterialJpaEntity.getStoragePrice()),
            toMoney(rawMaterialJpaEntity.getUnitPrice())
        );
    }

    private static Money toMoney(final MonetaryValueEmbeddable embeddable) {
        return new Money(embeddable.getAmount(), Currency.getInstance(embeddable.getCurrency()));
    }

    private static MonetaryValueEmbeddable toMonetaryValueEmbeddable(final Money money) {
        return new MonetaryValueEmbeddable(
            money.getAmount(),
            money.getCurrency().getCurrencyCode()
        );
    }
}
