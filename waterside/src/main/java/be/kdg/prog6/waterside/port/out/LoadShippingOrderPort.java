package be.kdg.prog6.waterside.port.out;

import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

import java.util.List;
import java.util.Optional;

public interface LoadShippingOrderPort {
    Optional<ShippingOrder> loadById(ShippingOrderId id);

    Optional<ShippingOrder> loadByReferenceId(ReferenceId referenceId); // PO reference

    List<ShippingOrder> loadShippingOrders();

    List<ShippingOrder> loadShippingOrdersOnSite();

    int countShippingOrdersOnSite();

    List<ShippingOrder> loadShippingOrdersWithQueuedBunkeringByOldestFirst();
}
