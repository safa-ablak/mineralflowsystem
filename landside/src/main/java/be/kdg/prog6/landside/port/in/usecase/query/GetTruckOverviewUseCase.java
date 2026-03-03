package be.kdg.prog6.landside.port.in.usecase.query;

import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;

@FunctionalInterface
public interface GetTruckOverviewUseCase {
    TruckOverview getTruckOverview(AppointmentId appointmentId);
}
