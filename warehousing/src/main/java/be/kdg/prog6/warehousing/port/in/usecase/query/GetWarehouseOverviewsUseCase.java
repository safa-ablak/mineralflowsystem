package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseOverview;

import java.util.List;

@FunctionalInterface
public interface GetWarehouseOverviewsUseCase {
    List<WarehouseOverview> getWarehouseOverviews();
}
