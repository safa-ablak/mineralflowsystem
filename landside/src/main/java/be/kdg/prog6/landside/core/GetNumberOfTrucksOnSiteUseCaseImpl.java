package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.port.in.usecase.query.GetNumberOfTrucksOnSiteUseCase;
import be.kdg.prog6.landside.port.out.LoadVisitPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetNumberOfTrucksOnSiteUseCaseImpl implements GetNumberOfTrucksOnSiteUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetNumberOfTrucksOnSiteUseCaseImpl.class);

    private final LoadVisitPort loadVisitPort;

    public GetNumberOfTrucksOnSiteUseCaseImpl(final LoadVisitPort loadVisitPort) {
        this.loadVisitPort = loadVisitPort;
    }

    @Override
    public int getNumberOfTrucksOnSite() {
        final LocalDate today = LocalDate.now();
        LOGGER.info("Checking number of trucks on site at {}", KDG);
        final int count = loadVisitPort.countActiveVisits();
        if (count == 0) {
            LOGGER.info("No Trucks are currently on site on: {}. Site is clear in case of emergency.", today);
        } else {
            LOGGER.info("There are currently {} Trucks on site on: {}. This is important for emergency management.",
                count,
                today
            );
        }
        return count;
    }
}
