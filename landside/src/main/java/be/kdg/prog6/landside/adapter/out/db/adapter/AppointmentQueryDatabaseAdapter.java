package be.kdg.prog6.landside.adapter.out.db.adapter;

import be.kdg.prog6.landside.adapter.out.db.entity.AppointmentJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.repository.AppointmentJpaRepository;
import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.port.out.AppointmentQueryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class AppointmentQueryDatabaseAdapter implements AppointmentQueryPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentQueryDatabaseAdapter.class);

    private final AppointmentJpaRepository appointmentJpaRepository;

    public AppointmentQueryDatabaseAdapter(final AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Override
    public Optional<Appointment> loadById(final AppointmentId id) {
        LOGGER.debug("Loading Appointment with ID {}", id.id()); // debug() to avoid log explosions in the console
        return appointmentJpaRepository.findById(id.id()).map(this::toAppointment);
    }

    @Override
    public List<Appointment> loadAppointmentsBetween(final LocalDateTime from, final LocalDateTime to) {
        LOGGER.info("Loading Appointments between {} and {}", from, to);
        return appointmentJpaRepository
            .findByArrivalWindowStartBetweenOrderByArrivalWindowStart(from, to)
            .stream()
            .map(this::toAppointment)
            .toList();
    }

    @Override
    public List<Appointment> loadAppointmentsByStatusAndWarehouseId(final AppointmentStatus status, final WarehouseId warehouseId) {
        LOGGER.info("Loading scheduled Appointments for Warehouse {}", warehouseId.id());
        return appointmentJpaRepository
            .findByWarehouseIdAndStatus(warehouseId.id(), status)
            .stream()
            .map(this::toAppointment)
            .toList();
    }

    private Appointment toAppointment(final AppointmentJpaEntity appointmentJpaEntity) {
        return new Appointment(
            AppointmentId.of(appointmentJpaEntity.getAppointmentId()),
            SupplierId.of(appointmentJpaEntity.getSupplierId()),
            WarehouseId.of(appointmentJpaEntity.getWarehouseId()),
            new TruckLicensePlate(appointmentJpaEntity.getTruckLicensePlate()),
            appointmentJpaEntity.getRawMaterial(),
            appointmentJpaEntity.getArrivalWindowStart(),
            appointmentJpaEntity.getStatus()
        );
    }
}
