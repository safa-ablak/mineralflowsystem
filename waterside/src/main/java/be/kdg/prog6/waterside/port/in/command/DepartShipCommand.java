package be.kdg.prog6.waterside.port.in.command;

import be.kdg.prog6.waterside.domain.ReferenceId;

import static java.util.Objects.requireNonNull;

public record DepartShipCommand(ReferenceId referenceId) { // Reference to the PO in the Warehousing Ctx
    public DepartShipCommand {
        requireNonNull(referenceId);
    }
}
