package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.Appointment;
import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.domain.exception.AppointmentNotFoundException;
import be.kdg.prog6.landside.port.in.usecase.query.GetTruckOverviewUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;
import be.kdg.prog6.landside.port.out.AppointmentQueryPort;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetTruckOverviewUseCaseImpl implements GetTruckOverviewUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTruckOverviewUseCaseImpl.class);

    private final AppointmentQueryPort appointmentQueryPort;
    private final LoadVisitPort loadVisitPort;

    public GetTruckOverviewUseCaseImpl(final AppointmentQueryPort appointmentQueryPort,
                                       final LoadVisitPort loadVisitPort) {
        this.appointmentQueryPort = appointmentQueryPort;
        this.loadVisitPort = loadVisitPort;
    }

    @Override
    public TruckOverview getTruckOverview(final AppointmentId appointmentId) {
        LOGGER.info("Getting Truck Overview for Appointment ID {}", appointmentId.id());
        final Appointment appointment = appointmentQueryPort.loadById(appointmentId).orElseThrow(
            () -> AppointmentNotFoundException.forId(appointmentId)
        );
        final Optional<Visit> optionalVisit = loadVisitPort.loadVisitByAppointmentId(appointmentId);
        return TruckOverview.from(appointment, optionalVisit);
    }
}
