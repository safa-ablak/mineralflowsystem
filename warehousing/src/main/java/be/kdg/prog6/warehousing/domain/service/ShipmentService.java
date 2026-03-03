package be.kdg.prog6.warehousing.domain.service;

import be.kdg.prog6.warehousing.domain.purchaseorder.OrderLine;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.ShipmentRecord;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ships raw materials for order lines of a purchase order from available warehouse deliveries.
 */
public class ShipmentService {
    /**
     * Ships all raw materials listed in the purchase order from the given warehouses.
     *
     * @param purchaseOrder The purchase order to ship raw materials for.
     * @param warehouses    List of all available warehouses.
     * @return A map with warehouses as keys and their respective shipment records as the values.
     */
    public Map<Warehouse, ShipmentRecord> shipForPurchaseOrderFrom(
        final PurchaseOrder purchaseOrder,
        final List<Warehouse> warehouses
    ) {
        final Map<Warehouse, ShipmentRecord> warehouseToShipmentRecord = new HashMap<>();

        for (OrderLine line : purchaseOrder.getOrderLines()) {
            shipForOrderLine(line, warehouses, warehouseToShipmentRecord);
        }

        return warehouseToShipmentRecord;
    }

    /**
     * Ships from available deliveries for the order line from eligible warehouses.
     *
     * @param orderLine                 The order line containing the raw material and amount
     * @param warehouses                All warehouses to consider.
     * @param warehouseToShipmentRecord Map to collect shipment records per warehouse.
     */
    private void shipForOrderLine(
        final OrderLine orderLine,
        final List<Warehouse> warehouses,
        final Map<Warehouse, ShipmentRecord> warehouseToShipmentRecord
    ) {
        BigDecimal remaining = orderLine.getAmount();
        final RawMaterial rawMaterial = orderLine.getRawMaterial();

        // In our case this will be a single warehouse (since 1 Warehouse/RawMaterial/Seller)
        final List<Warehouse> eligibleWarehouses = warehouses.stream()
            .filter(wh -> wh.isRawMaterialAssigned() && wh.getRawMaterial().equals(rawMaterial))
            .toList();

        for (Warehouse eligibleWarehouse : eligibleWarehouses) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            final ShipmentRecord shipmentRecord = eligibleWarehouse.recordShipment(remaining);
            remaining = remaining.subtract(shipmentRecord.shipment().amount());

            warehouseToShipmentRecord.put(eligibleWarehouse, shipmentRecord);
        }
    }
}
