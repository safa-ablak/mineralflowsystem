package be.kdg.prog6.warehousing.domain.purchaseorder;

import be.kdg.prog6.common.domain.measurement.Weight;
import be.kdg.prog6.common.domain.measurement.WeightUnit;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.exception.purchaseorder.DuplicateRawMaterialException;
import be.kdg.prog6.warehousing.domain.exception.purchaseorder.EmptyPurchaseOrderException;
import be.kdg.prog6.warehousing.domain.exception.purchaseorder.PurchaseOrderAlreadyFulfilledException;
import be.kdg.prog6.warehousing.domain.exception.purchaseorder.PurchaseOrderWeightLimitExceededException;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// The PurchaseOrder is the aggregate root
public class PurchaseOrder {
    /*
    A shipping order can take up to 150 kt of raw material; we assume that each ship can carry its own shipping order.
    */
    private static final Weight MAX_WEIGHT = new Weight(WeightUnit.TONS, BigDecimal.valueOf(150_000));

    /*
    From the Project Description: "Purchase Order: A purchase order contains a date,
    purchase order number, customer number, name and a number of purchase order lines."
    */
    private final PurchaseOrderId purchaseOrderId; // The 'reference' id
    private final PurchaseOrderNumber poNumber; // In the format 'PO-123456'
    private final BuyerId buyerId;
    private final String buyerName; // Note: If name of the buyer in the buyers table ever changes, this will become stale. Another option would be to write a custom query in the repo joining purchase_orders table with sellers table (Not sure how performant that would be, placing an index could help)
    private final SellerId sellerId;
    private final String sellerName; // Note: If name of the seller in the sellers table ever changes, this will become stale. Another option would be to write a custom query in the repo joining purchase_orders table with buyers table (Not sure how performant that would be, placing an index could help)
    private final LocalDateTime orderDate;
    private String vesselNumber; // To be filled in after a SO is entered in the Waterside BC and listened in Warehousing BC
    private final List<OrderLine> orderLines;
    private PurchaseOrderStatus status;

    /**
     * Constructor for creating a new purchase order.
     * Validates the order lines and ensures consistency.
     *
     * @param poNumber   The purchase order number.
     * @param buyerId    The buyer associated with the purchase order.
     * @param sellerId   The seller associated with the purchase order.
     * @param orderLines The initial order lines for the purchase order.
     */
    public PurchaseOrder(
        final PurchaseOrderNumber poNumber,
        final BuyerId buyerId,
        final String buyerName,
        final SellerId sellerId,
        final String sellerName,
        final List<OrderLine> orderLines
    ) {
        this.purchaseOrderId = PurchaseOrderId.newId();
        this.poNumber = poNumber;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.orderDate = LocalDateTime.now();
        this.vesselNumber = null;
        this.orderLines = new ArrayList<>();
        validateAndSetOrderLines(orderLines);
        this.status = PurchaseOrderStatus.PENDING;
    }

    /**
     * Constructor for mapping from persistence or with existing data.
     * Assumes data is already validated and consistent.
     *
     * @param purchaseOrderId The unique ID of the purchase order.
     * @param buyerId         The buyer associated with the purchase order.
     * @param sellerId        The seller associated with the purchase order.
     * @param orderDate       The date the order was created.
     * @param status          The status of the purchase order.
     * @param orderLines      The list of order lines.
     */
    public PurchaseOrder(final PurchaseOrderId purchaseOrderId, final PurchaseOrderNumber poNumber, final BuyerId buyerId, final String buyerName, final SellerId sellerId, final String sellerName, final LocalDateTime orderDate, final String vesselNumber, final PurchaseOrderStatus status, final List<OrderLine> orderLines) {
        this.purchaseOrderId = purchaseOrderId;
        this.poNumber = poNumber;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.orderDate = orderDate;
        this.vesselNumber = vesselNumber;
        this.status = status;
        this.orderLines = orderLines;
    }

    /**
     * Validates the provided list of order lines and sets them for the purchase order.
     * Ensures the total amount does not exceed the allowed limit and no duplicate raw materials are present.
     *
     * @param orderLines The list of order lines to validate and set.
     * @throws be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException If the total amount exceeds the limit or duplicate materials are found.
     */
    private void validateAndSetOrderLines(final List<OrderLine> orderLines) {
        ensureNotEmpty(orderLines);
        ensureNoDuplicateRawMaterials(orderLines);
        ensureTotalAmountWithinLimit(orderLines);
        assignLineNumbers(orderLines); // Assign line numbers to the order lines sequentially
        this.orderLines.addAll(orderLines);
    }

    private void ensureNotEmpty(final List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw EmptyPurchaseOrderException.forId(purchaseOrderId);
        }
    }

    /**
     * Validates that no duplicate raw materials exist in the given order lines.
     * <p>
     * Uses a {@link HashSet} to ensure all {@link RawMaterial} values are unique.
     *
     * @param orderLines The list of order lines to check.
     * @throws DuplicateRawMaterialException if duplicate raw materials are detected.
     */
    private void ensureNoDuplicateRawMaterials(final List<OrderLine> orderLines) {
        final Set<RawMaterial> uniqueRawMaterials = new HashSet<>();

        for (OrderLine orderLine : orderLines) {
            final RawMaterial rawMaterial = orderLine.getRawMaterial();
            if (!uniqueRawMaterials.add(rawMaterial)) {
                throw DuplicateRawMaterialException.forRawMaterial(rawMaterial);
            }
        }
    }

    /**
     * Ensures that the total amount of all order lines does not exceed the allowed limit.
     *
     * @param orderLines The list of order lines to validate.
     * @throws PurchaseOrderWeightLimitExceededException If the total amount exceeds the maximum allowed limit.
     */
    private void ensureTotalAmountWithinLimit(final List<OrderLine> orderLines) {
        final BigDecimal totalAmount = calculateTotalAmount(orderLines);

        if (totalAmount.compareTo(MAX_WEIGHT.amount()) > 0) {
            throw PurchaseOrderWeightLimitExceededException.forLimit(MAX_WEIGHT);
        }
    }

    /**
     * Assigns sequential line numbers (1, 2, 3...) to the order lines.
     * This ensures the PurchaseOrder controls line numbering.
     */
    private void assignLineNumbers(final List<OrderLine> orderLines) {
        int lineNumber = 1;
        for (OrderLine orderLine : orderLines) {
            orderLine.setLineNumber(lineNumber++);
        }
    }

    /**
     * Calculates the total amount in tons for the purchase order.
     *
     * @return The total amount in tons.
     */
    public BigDecimal calculateTotalAmount() {
        return orderLines.stream()
            .map(OrderLine::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Helper to calculate the total amount of a given list of order lines.
     *
     * @param orderLines The list of order lines.
     * @return The total amount in tons.
     */
    private BigDecimal calculateTotalAmount(final List<OrderLine> orderLines) {
        return orderLines.stream()
            .map(OrderLine::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isPending() {
        return this.status == PurchaseOrderStatus.PENDING;
    }

    public void fulfill() {
        ensureNotFulfilled();
        setStatus(PurchaseOrderStatus.FULFILLED);
    }

    // Guard against duplicate fulfillment to enforce domain invariants
    private void ensureNotFulfilled() {
        if (isFulfilled()) {
            throw PurchaseOrderAlreadyFulfilledException.forId(purchaseOrderId);
        }
    }

    public boolean isFulfilled() {
        return this.status == PurchaseOrderStatus.FULFILLED;
    }

    public PurchaseOrderId getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public PurchaseOrderNumber getPoNumber() {
        return poNumber;
    }

    public BuyerId getBuyerId() {
        return buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public SellerId getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getVesselNumber() {
        return vesselNumber;
    }

    public void fillVesselNumber(final String vesselNumber) {
        setVesselNumber(vesselNumber);
    }

    private void setVesselNumber(final String vesselNumber) {
        this.vesselNumber = vesselNumber;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    private void setStatus(final PurchaseOrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
            "PurchaseOrder{purchaseOrderId=%s, poNumber=%s, buyerId=%s, sellerId=%s, orderDate=%s, status=%s}",
            purchaseOrderId.id(), poNumber, buyerId.id(), sellerId.id(), orderDate, status
        );
    }
}
