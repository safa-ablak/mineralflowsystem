package be.kdg.prog6.warehousing.core.factory;

import be.kdg.prog6.warehousing.domain.Buyer;
import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.purchaseorder.OrderLine;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderNumber;
import be.kdg.prog6.warehousing.port.out.GeneratePurchaseOrderNumberPort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory for creating {@link PurchaseOrder} aggregates.
 * <p>
 * As Vaughn Vernon notes in <em>Implementing Domain-Driven Design</em> (Chapter 11):
 * <blockquote>
 * "An object that has the purpose only of instantiating a specific Aggregate type
 * will have no other responsibilities and will not even be considered a first-class
 * citizen of the model. It is only a Factory."
 * </blockquote>
 * This factory lives in the application layer (not the domain) because it depends
 * on an infrastructure port ({@link GeneratePurchaseOrderNumberPort}) for PO number generation.
 */
@Component
public final class PurchaseOrderFactory {
    private final GeneratePurchaseOrderNumberPort generatePurchaseOrderNumberPort;

    public PurchaseOrderFactory(final GeneratePurchaseOrderNumberPort generatePurchaseOrderNumberPort) {
        this.generatePurchaseOrderNumberPort = generatePurchaseOrderNumberPort;
    }

    public PurchaseOrder createPurchaseOrder(
        final Buyer buyer,
        final Seller seller,
        final List<OrderLine> orderLines
    ) {
        final PurchaseOrderNumber poNumber = generatePurchaseOrderNumberPort.generatePurchaseOrderNumber();
        return new PurchaseOrder(
            poNumber,
            buyer.getBuyerId(),
            buyer.getName(),
            seller.getSellerId(),
            seller.getName(),
            orderLines
        );
    }
}
