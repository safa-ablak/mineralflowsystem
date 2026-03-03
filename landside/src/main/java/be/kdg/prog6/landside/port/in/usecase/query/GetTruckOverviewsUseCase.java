package be.kdg.prog6.landside.port.in.usecase.query;

import be.kdg.prog6.landside.port.in.query.GetTruckOverviewsQuery;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;

import java.util.List;

@FunctionalInterface
public interface GetTruckOverviewsUseCase {
    List<TruckOverview> getTruckOverviews(GetTruckOverviewsQuery query);
}
