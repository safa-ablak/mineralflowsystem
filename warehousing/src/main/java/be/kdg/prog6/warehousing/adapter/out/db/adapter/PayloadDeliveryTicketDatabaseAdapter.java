package be.kdg.prog6.warehousing.adapter.out.db.adapter;

import be.kdg.prog6.warehousing.adapter.out.db.entity.PayloadDeliveryTicketJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.db.repository.PayloadDeliveryTicketJpaRepository;
import be.kdg.prog6.warehousing.domain.storage.PayloadDeliveryTicket;
import be.kdg.prog6.warehousing.domain.storage.PayloadDeliveryTicketId;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.out.CreatePayloadDeliveryTicketPort;
import be.kdg.prog6.warehousing.port.out.LoadPayloadDeliveryTicketPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayloadDeliveryTicketDatabaseAdapter implements CreatePayloadDeliveryTicketPort, LoadPayloadDeliveryTicketPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadDeliveryTicketDatabaseAdapter.class);

    private final PayloadDeliveryTicketJpaRepository payloadDeliveryTicketJpaRepository;

    public PayloadDeliveryTicketDatabaseAdapter(final PayloadDeliveryTicketJpaRepository payloadDeliveryTicketJpaRepository) {
        this.payloadDeliveryTicketJpaRepository = payloadDeliveryTicketJpaRepository;
    }

    @Override
    public List<PayloadDeliveryTicket> loadTicketsByWarehouseId(final WarehouseId warehouseId) {
        LOGGER.info("Loading Payload Delivery Tickets By Warehouse ID {}", warehouseId.id());
        return payloadDeliveryTicketJpaRepository.findByWarehouseId(warehouseId.id())
            .stream()
            .map(this::toPayloadDeliveryTicket)
            .toList();
    }

    @Override
    public void createPayloadDeliveryTicket(final PayloadDeliveryTicket payloadDeliveryTicket) {
        LOGGER.info("Creating Payload Delivery Ticket with ID {}", payloadDeliveryTicket.id().id());
        final PayloadDeliveryTicketJpaEntity jpaEntity = toPayloadDeliveryTicketJpaEntity(payloadDeliveryTicket);
        payloadDeliveryTicketJpaRepository.save(jpaEntity);
    }

    private PayloadDeliveryTicket toPayloadDeliveryTicket(final PayloadDeliveryTicketJpaEntity pdtJpaEntity) {
        return new PayloadDeliveryTicket(
            PayloadDeliveryTicketId.of(pdtJpaEntity.getTicketId()),
            pdtJpaEntity.getRawMaterial(),
            pdtJpaEntity.getGenerationTime(),
            WarehouseId.of(pdtJpaEntity.getWarehouseId()),
            pdtJpaEntity.getWarehouseNumber(),
            pdtJpaEntity.getDockNumber()
        );
    }

    private PayloadDeliveryTicketJpaEntity toPayloadDeliveryTicketJpaEntity(final PayloadDeliveryTicket pdt) {
        final PayloadDeliveryTicketJpaEntity pdtJpaEntity = new PayloadDeliveryTicketJpaEntity();
        pdtJpaEntity.setTicketId(pdt.id().id());
        pdtJpaEntity.setRawMaterial(pdt.rawMaterial());
        pdtJpaEntity.setGenerationTime(pdt.generationTime());
        pdtJpaEntity.setWarehouseId(pdt.warehouseId().id());
        pdtJpaEntity.setWarehouseNumber(pdt.warehouseNumber());
        pdtJpaEntity.setDockNumber(pdt.dockNumber());
        return pdtJpaEntity;
    }
}
