package be.kdg.prog6.invoicing.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.invoicing.domain.RawMaterialId;

public class RawMaterialNotFoundException extends NotFoundException {
    private RawMaterialNotFoundException(String message) {
        super(message);
    }

    public static RawMaterialNotFoundException forName(final String name) {
        return new RawMaterialNotFoundException(
            String.format("Raw material with name '%s' not found", name)
        );
    }

    public static RawMaterialNotFoundException forId(final RawMaterialId id) {
        return new RawMaterialNotFoundException(
            String.format("Raw material with ID %s not found", id.id())
        );
    }
}
