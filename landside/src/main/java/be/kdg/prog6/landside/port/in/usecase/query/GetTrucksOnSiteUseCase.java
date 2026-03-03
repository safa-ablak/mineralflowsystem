package be.kdg.prog6.landside.port.in.usecase.query;

import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;

import java.util.List;

@FunctionalInterface
public interface GetTrucksOnSiteUseCase {
    List<TruckOverview> getTrucksOnSite();
}
