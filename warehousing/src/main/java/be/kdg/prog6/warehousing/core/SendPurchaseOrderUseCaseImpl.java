package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.core.factory.PurchaseOrderFactory;
import be.kdg.prog6.warehousing.domain.Buyer;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.exception.BuyerNotFoundException;
import be.kdg.prog6.warehousing.domain.exception.SellerNotFoundException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.port.in.command.SendPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.usecase.SendPurchaseOrderUseCase;
import be.kdg.prog6.warehousing.port.out.CreatePurchaseOrderPort;
import be.kdg.prog6.warehousing.port.out.LoadBuyerPort;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendPurchaseOrderUseCaseImpl implements SendPurchaseOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendPurchaseOrderUseCaseImpl.class);

    private final LoadSellerPort loadSellerPort;
    private final LoadBuyerPort loadBuyerPort;
    private final PurchaseOrderFactory purchaseOrderFactory;
    private final CreatePurchaseOrderPort createPurchaseOrderPort;

    public SendPurchaseOrderUseCaseImpl(final LoadSellerPort loadSellerPort,
                                        final LoadBuyerPort loadBuyerPort,
                                        final PurchaseOrderFactory purchaseOrderFactory,
                                        final CreatePurchaseOrderPort createPurchaseOrderPort) {
        this.loadSellerPort = loadSellerPort;
        this.loadBuyerPort = loadBuyerPort;
        this.purchaseOrderFactory = purchaseOrderFactory;
        this.createPurchaseOrderPort = createPurchaseOrderPort;
    }

    @Override
    @Transactional
    public PurchaseOrder sendPurchaseOrder(final SendPurchaseOrderCommand command) {
        final BuyerId buyerId = command.buyerId();
        final SellerId sellerId = command.sellerId();
        LOGGER.info("Attempting to send Purchase Order (Buyer ID {}, Seller ID {})",
            buyerId.id(), sellerId.id()
        );
        final Buyer buyer = loadBuyerPort.loadBuyerById(buyerId).orElseThrow(
            () -> BuyerNotFoundException.forId(buyerId)
        );
        final Seller seller = loadSellerPort.loadSellerById(sellerId).orElseThrow(
            () -> SellerNotFoundException.forId(sellerId)
        );
        LOGGER.info("Buyer and Seller exist, proceeding to send the Purchase Order");
        final PurchaseOrder purchaseOrder = purchaseOrderFactory.createPurchaseOrder(
            buyer,
            seller,
            command.orderLines()
        );
        createPurchaseOrderPort.createPurchaseOrder(purchaseOrder);
        LOGGER.info("Purchase Order with ID {} has been sent",
            purchaseOrder.getPurchaseOrderId().id()
        );
        return purchaseOrder;
    }
}
