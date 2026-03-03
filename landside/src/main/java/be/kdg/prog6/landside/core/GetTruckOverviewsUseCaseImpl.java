package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.Appointment;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.port.in.query.GetTruckOverviewsQuery;
import be.kdg.prog6.landside.port.in.usecase.query.GetTruckOverviewsUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;
import be.kdg.prog6.landside.port.out.AppointmentQueryPort;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetTruckOverviewsUseCaseImpl implements GetTruckOverviewsUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTruckOverviewsUseCaseImpl.class);

    private final AppointmentQueryPort appointmentQueryPort;
    private final LoadVisitPort loadVisitPort;

    public GetTruckOverviewsUseCaseImpl(final AppointmentQueryPort appointmentQueryPort,
                                        final LoadVisitPort loadVisitPort) {
        this.appointmentQueryPort = appointmentQueryPort;
        this.loadVisitPort = loadVisitPort;
    }

    @Override
    public List<TruckOverview> getTruckOverviews(final GetTruckOverviewsQuery query) {
        final LocalDate from = query.from();
        final LocalDate to = query.to();

        LOGGER.info("Getting Truck Overviews between {} and {} at {}", from, to, KDG);
        final List<Appointment> appointments = appointmentQueryPort.loadAppointmentsBetween(
            from.atStartOfDay(),
            to.atStartOfDay().plusDays(1)
        );
        final List<TruckOverview> truckOverviews = new ArrayList<>();
        for (Appointment appointment : appointments) {
            final Optional<Visit> optionalVisit =
                loadVisitPort.loadVisitByAppointmentId(appointment.getAppointmentId());
            truckOverviews.add(TruckOverview.from(appointment, optionalVisit));
        }
        LOGGER.info("Found {} Truck Overviews between {} and {}", truckOverviews.size(), from, to);
        return truckOverviews;
    }
}
