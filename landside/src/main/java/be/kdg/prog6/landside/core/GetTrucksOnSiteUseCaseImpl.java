package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.Appointment;
import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.port.in.usecase.query.GetTrucksOnSiteUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;
import be.kdg.prog6.landside.port.out.AppointmentQueryPort;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetTrucksOnSiteUseCaseImpl implements GetTrucksOnSiteUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTrucksOnSiteUseCaseImpl.class);

    private final AppointmentQueryPort appointmentQueryPort;
    private final LoadVisitPort loadVisitPort;

    public GetTrucksOnSiteUseCaseImpl(final AppointmentQueryPort appointmentQueryPort,
                                      final LoadVisitPort loadVisitPort) {
        this.appointmentQueryPort = appointmentQueryPort;
        this.loadVisitPort = loadVisitPort;
    }

    @Override
    public List<TruckOverview> getTrucksOnSite() {
        final List<TruckOverview> truckOverviews = new ArrayList<>();
        LOGGER.info("Getting trucks currently on site at {}", KDG);
        final List<Visit> visits = loadVisitPort.loadAllActiveVisits();

        for (Visit visit : visits) {
            final AppointmentId appointmentId = visit.getAppointmentId();
            final Appointment appointment = appointmentQueryPort.loadById(appointmentId).orElseThrow();
            truckOverviews.add(TruckOverview.from(appointment, Optional.of(visit)));
        }
        LOGGER.info("Found {} Trucks currently on site", truckOverviews.size());
        return truckOverviews;
    }
}
