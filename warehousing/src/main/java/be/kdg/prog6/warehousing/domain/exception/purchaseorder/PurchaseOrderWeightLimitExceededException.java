package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.common.domain.measurement.Weight;
import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;

import static be.kdg.prog6.common.domain.measurement.WeightUnit.KILOTONS;

public class PurchaseOrderWeightLimitExceededException extends WarehousingDomainException {
    private PurchaseOrderWeightLimitExceededException(final String message) {
        super(message);
    }

    public static PurchaseOrderWeightLimitExceededException forLimit(final Weight limit) {
        return new PurchaseOrderWeightLimitExceededException(
            "Purchase Order total weight exceeds the maximum allowed limit of %s"
                .formatted(limit.toUnit(KILOTONS))
        );
    }
}
