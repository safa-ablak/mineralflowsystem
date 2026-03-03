package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.port.in.command.AssignRawMaterialToWarehouseCommand;

@FunctionalInterface
public interface AssignRawMaterialToWarehouseUseCase {
    void assignRawMaterial(AssignRawMaterialToWarehouseCommand command);
}
