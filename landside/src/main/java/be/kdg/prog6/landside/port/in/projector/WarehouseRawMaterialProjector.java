package be.kdg.prog6.landside.port.in.projector;

import be.kdg.prog6.landside.port.in.command.ProjectRawMaterialCommand;

@FunctionalInterface
public interface WarehouseRawMaterialProjector {
    void projectRawMaterial(ProjectRawMaterialCommand command);
}
