package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.exception.storage.WarehouseNotFoundException;
import be.kdg.prog6.warehousing.domain.storage.PayloadDeliveryTicket;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.domain.storage.WarehouseNumber;
import be.kdg.prog6.warehousing.port.in.command.GeneratePayloadDeliveryTicketCommand;
import be.kdg.prog6.warehousing.port.in.usecase.GeneratePayloadDeliveryTicketUseCase;
import be.kdg.prog6.warehousing.port.out.CreatePayloadDeliveryTicketPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneratePayloadDeliveryTicketUseCaseImpl implements GeneratePayloadDeliveryTicketUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePayloadDeliveryTicketUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final List<CreatePayloadDeliveryTicketPort> createPayloadDeliveryTicketPorts;

    public GeneratePayloadDeliveryTicketUseCaseImpl(final LoadWarehousePort loadWarehousePort,
                                                    final List<CreatePayloadDeliveryTicketPort> createPayloadDeliveryTicketPorts) {
        this.loadWarehousePort = loadWarehousePort;
        this.createPayloadDeliveryTicketPorts = createPayloadDeliveryTicketPorts;
    }

    @Override
    @Transactional
    public void generatePayloadDeliveryTicket(final GeneratePayloadDeliveryTicketCommand command) {
        final WarehouseId warehouseId = command.warehouseId();
        final String dockNumber = command.dockNumber();

        LOGGER.info("Attempting to generate Payload Delivery Ticket (PDT) for Warehouse ID {} and Dock Number {}",
            warehouseId.id(), dockNumber
        );
        // Load the warehouse via ID to get the warehouse number.
        final Warehouse warehouse = loadWarehousePort.loadWarehouseById(warehouseId).orElseThrow(
            () -> WarehouseNotFoundException.forId(warehouseId)
        );
        final String rawMaterial = warehouse.getRawMaterial().getDisplayName();
        final WarehouseNumber warehouseNumber = warehouse.getWarehouseNumber();

        LOGGER.info("Generating PDT for Raw Material {}, Warehouse Number {}, Dock Number {}",
            rawMaterial, warehouseNumber, dockNumber
        );
        final PayloadDeliveryTicket pdt = new PayloadDeliveryTicket(
            rawMaterial,
            warehouse.getWarehouseId(),
            warehouseNumber.value(),
            dockNumber
        );
        createPayloadDeliveryTicketPorts.forEach(
            port -> port.createPayloadDeliveryTicket(pdt)
        );
    }
}
