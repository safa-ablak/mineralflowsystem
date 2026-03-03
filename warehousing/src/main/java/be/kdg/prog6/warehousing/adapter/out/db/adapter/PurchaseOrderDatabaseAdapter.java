package be.kdg.prog6.warehousing.adapter.out.db.adapter;

import be.kdg.prog6.warehousing.adapter.out.db.entity.OrderLineJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.db.entity.PurchaseOrderJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.db.repository.PurchaseOrderJpaRepository;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.*;
import be.kdg.prog6.warehousing.port.out.CreatePurchaseOrderPort;
import be.kdg.prog6.warehousing.port.out.LoadPurchaseOrderPort;
import be.kdg.prog6.warehousing.port.out.UpdatePurchaseOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderDatabaseAdapter implements CreatePurchaseOrderPort, LoadPurchaseOrderPort, UpdatePurchaseOrderPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderDatabaseAdapter.class);

    private final PurchaseOrderJpaRepository purchaseOrderJpaRepository;

    public PurchaseOrderDatabaseAdapter(final PurchaseOrderJpaRepository purchaseOrderJpaRepository) {
        this.purchaseOrderJpaRepository = purchaseOrderJpaRepository;
    }

    @Override
    public Optional<PurchaseOrder> loadPurchaseOrderById(final PurchaseOrderId id) {
        LOGGER.info("Loading Purchase order with ID {}", id.id());
        return purchaseOrderJpaRepository.findById(id.id()).map(this::toPurchaseOrder);
    }

    @Override
    public List<PurchaseOrder> loadPurchaseOrdersBy(final PurchaseOrderStatus status, final String sellerName) {
        LOGGER.info("Loading Purchase Orders by Status: {}, Seller Name: {}", status, sellerName);
        return purchaseOrderJpaRepository.findByStatusAndSellerName(status, sellerName)
            .stream()
            .map(this::toPurchaseOrder)
            .toList();
    }

    @Override
    public Optional<PurchaseOrder> loadPurchaseOrderByBuyerIdAndIdAndStatus(
        final BuyerId buyerId,
        final PurchaseOrderId purchaseOrderId,
        final PurchaseOrderStatus status
    ) {
        LOGGER.info("Loading Purchase Order with Buyer ID {} ID {} and Status {}",
            buyerId.id(), purchaseOrderId.id(), status
        );
        return purchaseOrderJpaRepository
            .findByBuyerIdAndPurchaseOrderIdAndStatus(buyerId.id(), purchaseOrderId.id(), status)
            .map(this::toPurchaseOrder);
    }

    @Override
    public void createPurchaseOrder(final PurchaseOrder purchaseOrder) {
        LOGGER.info("Creating Purchase Order with ID {}", purchaseOrder.getPurchaseOrderId().id());
        final PurchaseOrderJpaEntity purchaseOrderEntity = toPurchaseOrderJpaEntity(purchaseOrder);
        purchaseOrderJpaRepository.save(purchaseOrderEntity);
    }

    @Override
    public void updatePurchaseOrder(final PurchaseOrder purchaseOrder) {
        LOGGER.info("Updating Purchase Order with ID {}", purchaseOrder.getPurchaseOrderId().id());
        final PurchaseOrderJpaEntity purchaseOrderEntity = toPurchaseOrderJpaEntity(purchaseOrder);
        purchaseOrderJpaRepository.save(purchaseOrderEntity);
    }

    private PurchaseOrderJpaEntity toPurchaseOrderJpaEntity(final PurchaseOrder purchaseOrder) {
        final PurchaseOrderJpaEntity poJpaEntity = new PurchaseOrderJpaEntity();
        poJpaEntity.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId().id());
        poJpaEntity.setPoNumber(purchaseOrder.getPoNumber().value());
        poJpaEntity.setBuyerId(purchaseOrder.getBuyerId().id());
        poJpaEntity.setBuyerName(purchaseOrder.getBuyerName());
        poJpaEntity.setSellerId(purchaseOrder.getSellerId().id());
        poJpaEntity.setSellerName(purchaseOrder.getSellerName());
        poJpaEntity.setOrderDate(purchaseOrder.getOrderDate());
        poJpaEntity.setVesselNumber(purchaseOrder.getVesselNumber());
        poJpaEntity.setStatus(purchaseOrder.getStatus());
        final List<OrderLineJpaEntity> olJpaEntities = purchaseOrder.getOrderLines()
            .stream()
            .map(orderLine -> toJpaOrderLineEntity(orderLine, poJpaEntity))
            .toList();
        poJpaEntity.setOrderLines(olJpaEntities);
        return poJpaEntity;
    }

    private OrderLineJpaEntity toJpaOrderLineEntity(final OrderLine orderLine, final PurchaseOrderJpaEntity parent) {
        final OrderLineJpaEntity olJpaEntity = new OrderLineJpaEntity();
        olJpaEntity.setOrderLineId(orderLine.getId().id());
        olJpaEntity.setLineNumber(orderLine.getLineNumber());
        olJpaEntity.setRawMaterial(orderLine.getRawMaterial());
        olJpaEntity.setAmount(orderLine.getAmount());
        olJpaEntity.setPurchaseOrder(parent);
        return olJpaEntity;
    }

    private PurchaseOrder toPurchaseOrder(final PurchaseOrderJpaEntity purchaseOrderEntity) {
        final List<OrderLine> orderLines = purchaseOrderEntity.getOrderLines()
            .stream()
            .map(this::toOrderLine)
            .collect(Collectors.toList());
        return new PurchaseOrder(
            PurchaseOrderId.of(purchaseOrderEntity.getPurchaseOrderId()),
            new PurchaseOrderNumber(purchaseOrderEntity.getPoNumber()),
            BuyerId.of(purchaseOrderEntity.getBuyerId()),
            purchaseOrderEntity.getBuyerName(),
            SellerId.of(purchaseOrderEntity.getSellerId()),
            purchaseOrderEntity.getSellerName(),
            purchaseOrderEntity.getOrderDate(),
            purchaseOrderEntity.getVesselNumber(),
            purchaseOrderEntity.getStatus(),
            orderLines
        );
    }

    private OrderLine toOrderLine(final OrderLineJpaEntity orderLineEntity) {
        return new OrderLine(
            OrderLineId.of(orderLineEntity.getOrderLineId()),
            orderLineEntity.getLineNumber(),
            orderLineEntity.getRawMaterial(),
            orderLineEntity.getAmount()
        );
    }
}
