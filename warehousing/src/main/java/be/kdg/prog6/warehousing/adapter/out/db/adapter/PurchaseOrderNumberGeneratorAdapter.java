package be.kdg.prog6.warehousing.adapter.out.db.adapter;

import be.kdg.prog6.warehousing.adapter.out.db.repository.PurchaseOrderJpaRepository;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderNumber;
import be.kdg.prog6.warehousing.port.out.GeneratePurchaseOrderNumberPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Generates sequential purchase order numbers based on the total count of purchase orders in the system.
 * This implementation relies on the {@link PurchaseOrderJpaRepository}.
 */
@Component
public class PurchaseOrderNumberGeneratorAdapter implements GeneratePurchaseOrderNumberPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderNumberGeneratorAdapter.class);
    private static final String PO_NUMBER_FORMAT = "PO%06d";

    private final PurchaseOrderJpaRepository purchaseOrderJpaRepository;

    public PurchaseOrderNumberGeneratorAdapter(final PurchaseOrderJpaRepository purchaseOrderJpaRepository) {
        this.purchaseOrderJpaRepository = purchaseOrderJpaRepository;
    }

    @Override
    public PurchaseOrderNumber generatePurchaseOrderNumber() {
        final long count = purchaseOrderJpaRepository.count();
        final long nextNumber = count + 1;
        final String formattedNumber = String.format(PO_NUMBER_FORMAT, nextNumber);
        LOGGER.info("Generated next Purchase Order Number: {}", formattedNumber);
        return new PurchaseOrderNumber(formattedNumber);
    }
}
