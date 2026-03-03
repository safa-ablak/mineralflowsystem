package be.kdg.prog6.warehousing.domain.exception.purchaseorder;

import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;

public class DuplicateRawMaterialException extends WarehousingDomainException {
    private DuplicateRawMaterialException(String message) {
        super(message);
    }

    public static DuplicateRawMaterialException forRawMaterial(final RawMaterial rawMaterial) {
        return new DuplicateRawMaterialException(String.format(
            "Raw material '%s' appears more than once in this Purchase Order. Each raw material must appear only once.",
            rawMaterial.getDisplayName()
        ));
    }
}
