package be.kdg.prog6.landside.adapter.out.db.adapter;

import be.kdg.prog6.landside.adapter.out.db.entity.VisitJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.entity.WeighBridgeTransactionJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.repository.VisitJpaRepository;
import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import be.kdg.prog6.landside.port.out.UpdateVisitPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VisitDatabaseAdapter implements LoadVisitPort, UpdateVisitPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitDatabaseAdapter.class);

    private final VisitJpaRepository visitJpaRepository;

    public VisitDatabaseAdapter(final VisitJpaRepository visitJpaRepository) {
        this.visitJpaRepository = visitJpaRepository;
    }

    @Override
    public Optional<Visit> loadActiveVisitByTruckLicensePlate(final TruckLicensePlate truckLicensePlate) {
        LOGGER.info("Loading Active Visit By Truck License Plate: {}", truckLicensePlate);
        return visitJpaRepository
            .findActiveVisitByLicensePlate(truckLicensePlate.value())
            .map(this::toVisit);
    }

    @Override
    public List<Visit> loadAllActiveVisits() {
        LOGGER.info("Loading all active Visits");
        return visitJpaRepository.findAllActiveVisits()
            .stream()
            .map(this::toVisit)
            .toList();
    }

    @Override
    public int countActiveVisits() {
        LOGGER.info("Counting all active Visits");
        return visitJpaRepository.countActiveVisits();
    }

    @Override
    public Optional<Visit> loadVisitByAppointmentId(final AppointmentId appointmentId) {
        LOGGER.debug("Loading Visit by Appointment ID {}", appointmentId.id());
        return visitJpaRepository.findByAppointmentId(appointmentId.id()).map(this::toVisit);
    }

    @Override
    public void updateVisit(final Visit visit) {
        LOGGER.info("Updating Visit with ID {} and Status {}", visit.getVisitId().id(), visit.getStatus());
        final VisitJpaEntity entity = toVisitJpaEntity(visit);
        visitJpaRepository.save(entity);
    }

    private VisitJpaEntity toVisitJpaEntity(final Visit visit) {
        final VisitJpaEntity visitJpaEntity = new VisitJpaEntity();
        visitJpaEntity.setVisitId(visit.getVisitId().id());
        visitJpaEntity.setAppointmentId(visit.getAppointmentId().id());
        visitJpaEntity.setWarehouseId(visit.getWarehouseId().id());
        visitJpaEntity.setTruckLicensePlate(visit.getTruckLicensePlate().value());
        visitJpaEntity.setRawMaterial(visit.getRawMaterial());
        visitJpaEntity.setArrivalTime(visit.getArrivalTime());
        visitJpaEntity.setDockTime(visit.getDockTime());
        visitJpaEntity.setDepartureTime(visit.getDepartureTime());
        visitJpaEntity.setStatus(visit.getStatus());
        if (visit.hasWeighBridgeTransaction()) {
            visitJpaEntity.setWeighBridgeTransaction(toJpaWeighBridgeTransaction(visit.getWeighBridgeTransaction()));
        }
        return visitJpaEntity;
    }

    private WeighBridgeTransactionJpaEntity toJpaWeighBridgeTransaction(final WeighBridgeTransaction transaction) {
        final WeighBridgeTransactionJpaEntity transactionJpaEntity = new WeighBridgeTransactionJpaEntity();
        transactionJpaEntity.setTransactionId(transaction.getId().id());
        transactionJpaEntity.setTruckLicensePlate(transaction.getTruckLicensePlate().value());
        transactionJpaEntity.setGrossWeight(transaction.getGrossWeight());
        transactionJpaEntity.setTareWeight(transaction.getTareWeight());
        transactionJpaEntity.setWeighInTime(transaction.getWeighInTime());
        transactionJpaEntity.setWeighOutTime(transaction.getWeighOutTime());
        return transactionJpaEntity;
    }

    private Visit toVisit(final VisitJpaEntity jpaEntity) {
        final WeighBridgeTransaction transaction = jpaEntity.getWeighBridgeTransaction() != null
            ? toWeighBridgeTransaction(jpaEntity.getWeighBridgeTransaction())
            : null;
        return new Visit(
            VisitId.of(jpaEntity.getVisitId()),
            AppointmentId.of(jpaEntity.getAppointmentId()),
            WarehouseId.of(jpaEntity.getWarehouseId()),
            new TruckLicensePlate(jpaEntity.getTruckLicensePlate()),
            jpaEntity.getRawMaterial(),
            jpaEntity.getArrivalTime(),
            transaction,
            jpaEntity.getDockTime(),
            jpaEntity.getDepartureTime(),
            jpaEntity.getStatus()
        );
    }

    private WeighBridgeTransaction toWeighBridgeTransaction(final WeighBridgeTransactionJpaEntity jpaEntity) {
        return new WeighBridgeTransaction(
            WeighBridgeTransactionId.of(jpaEntity.getTransactionId()),
            new TruckLicensePlate(jpaEntity.getTruckLicensePlate()),
            jpaEntity.getGrossWeight(),
            jpaEntity.getTareWeight(),
            jpaEntity.getWeighInTime(),
            jpaEntity.getWeighOutTime()
        );
    }
}