package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.exception.purchaseorder.PurchaseOrderNotFoundException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetPurchaseOrderUseCase;
import be.kdg.prog6.warehousing.port.out.LoadPurchaseOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetPurchaseOrderUseCaseImpl implements GetPurchaseOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetPurchaseOrderUseCaseImpl.class);

    private final LoadPurchaseOrderPort loadPurchaseOrderPort;

    public GetPurchaseOrderUseCaseImpl(final LoadPurchaseOrderPort loadPurchaseOrderPort) {
        this.loadPurchaseOrderPort = loadPurchaseOrderPort;
    }

    @Override
    @Transactional
    public PurchaseOrder getPurchaseOrder(final PurchaseOrderId id) {
        LOGGER.info("Getting Purchase Order with ID {}", id.id());
        return loadPurchaseOrderPort.loadPurchaseOrderById(id).orElseThrow(
            () -> PurchaseOrderNotFoundException.forId(id)
        );
    }
}
