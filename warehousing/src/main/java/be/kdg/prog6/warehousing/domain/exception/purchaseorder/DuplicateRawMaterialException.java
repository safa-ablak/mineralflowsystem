package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;

public class DuplicateRawMaterialException extends WarehousingDomainException {
    public DuplicateRawMaterialException(final RawMaterial rawMaterial) {
        super(String.format(
            "Raw material '%s' appears more than once in this Purchase Order. Each raw material must appear only once.",
            rawMaterial.getDisplayName()
        ));
    }
}
