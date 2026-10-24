package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.common.domain.measurement.Weight;
import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;

import static be.kdg.prog6.common.domain.measurement.WeightUnit.KILOTONS;

public class PurchaseOrderWeightLimitExceededException extends WarehousingDomainException {
    public PurchaseOrderWeightLimitExceededException(final Weight limit) {
        super("Purchase Order total weight exceeds the maximum allowed limit of %s".formatted(limit.toUnit(KILOTONS)));
    }
}
