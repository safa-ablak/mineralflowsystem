package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.port.in.command.CancelMismatchedAppointmentsForWarehouseCommand;

@FunctionalInterface
public interface CancelMismatchedAppointmentsForWarehouseUseCase {
    void cancelAppointmentsWithMismatchedRawMaterial(CancelMismatchedAppointmentsForWarehouseCommand command);
}
