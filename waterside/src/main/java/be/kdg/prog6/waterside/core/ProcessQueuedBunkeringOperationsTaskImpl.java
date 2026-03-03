package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.service.BunkeringOperationService;
import be.kdg.prog6.waterside.port.in.usecase.scheduled.ProcessQueuedBunkeringOperationsTask;
import be.kdg.prog6.waterside.port.out.BunkeringOperationQueryPort;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class ProcessQueuedBunkeringOperationsTaskImpl implements ProcessQueuedBunkeringOperationsTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessQueuedBunkeringOperationsTaskImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;
    private final BunkeringOperationQueryPort bunkeringOperationQueryPort;
    private final BunkeringOperationService bunkeringOperationService;
    private final UpdateShippingOrderPort updateShippingOrderPort;
    private final Clock clock;

    public ProcessQueuedBunkeringOperationsTaskImpl(final LoadShippingOrderPort loadShippingOrderPort,
                                                    final BunkeringOperationQueryPort bunkeringOperationQueryPort,
                                                    final UpdateShippingOrderPort updateShippingOrderPort,
                                                    final Clock clock) {
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.bunkeringOperationQueryPort = bunkeringOperationQueryPort;
        this.bunkeringOperationService = new BunkeringOperationService(); // Instantiating the domain service
        this.updateShippingOrderPort = updateShippingOrderPort;
        this.clock = clock;
    }

    @Override
    @Scheduled(cron = "0 0 5 * * *", zone = "Europe/Brussels") // Runs every day at 05:00 AM
//    @Scheduled(cron = "0 * * * * *", zone = "Europe/Brussels") // For testing purposes, every minute.
    @Transactional
    public void processQueuedBunkeringOperations() { // FIFO
        final LocalDate processingDate = LocalDate.now(clock);
        LOGGER.info("Scheduled Bunkering Operation processing started at {} at {}", KDG, processingDate);
        int bunkeringOperationsProcessed = 0;

        final List<ShippingOrder> orders =
            loadShippingOrderPort.loadShippingOrdersWithQueuedBunkeringByOldestFirst();
        final int dailyOperationCount =
            bunkeringOperationQueryPort.countPerformedBunkeringOperationsByDate(processingDate);

        for (ShippingOrder order : orders) {
            final boolean canPerform = bunkeringOperationService.canPerformBunkering(
                dailyOperationCount + bunkeringOperationsProcessed
            );
            if (!canPerform) {
                LOGGER.warn("Bunkering limit reached. Stopping further processing.");
                break;
            }
            order.performBunkering();
            updateShippingOrderPort.updateShippingOrder(order);
            bunkeringOperationsProcessed++;
        }
        LOGGER.info("Scheduled Task completed. {} Bunkering Operations processed.", bunkeringOperationsProcessed);
    }
}
