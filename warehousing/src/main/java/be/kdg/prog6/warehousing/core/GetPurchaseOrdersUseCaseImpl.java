package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderStatus;
import be.kdg.prog6.warehousing.port.in.query.GetPurchaseOrdersQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetPurchaseOrdersUseCase;
import be.kdg.prog6.warehousing.port.out.LoadPurchaseOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPurchaseOrdersUseCaseImpl implements GetPurchaseOrdersUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetPurchaseOrdersUseCaseImpl.class);

    private final LoadPurchaseOrderPort loadPurchaseOrderPort;

    public GetPurchaseOrdersUseCaseImpl(final LoadPurchaseOrderPort loadPurchaseOrderPort) {
        this.loadPurchaseOrderPort = loadPurchaseOrderPort;
    }

    @Override
    @Transactional
    public List<PurchaseOrder> getPurchaseOrders(final GetPurchaseOrdersQuery query) {
        final PurchaseOrderStatus status = query.status();
        final String sellerName = query.sellerName();

        LOGGER.info("Getting Purchase Orders by Status {}, Seller Name: {}", status, sellerName);
        final List<PurchaseOrder> purchaseOrders = loadPurchaseOrderPort.loadPurchaseOrdersBy(status, sellerName);
        LOGGER.info("Retrieved {} Purchase Orders (Status: {}, Seller Name: {})",
            purchaseOrders.size(), status, sellerName
        );
        return purchaseOrders;
    }
}
