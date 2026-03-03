package be.kdg.prog6.waterside.core;

import be.kdg.prog6.common.security.UserRole;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.domain.service.BunkeringOperationService;
import be.kdg.prog6.waterside.port.in.command.BunkerShipCommand;
import be.kdg.prog6.waterside.port.in.usecase.BunkerShipUseCase;
import be.kdg.prog6.waterside.port.out.BunkeringOperationQueryPort;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class BunkerShipUseCaseImpl implements BunkerShipUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BunkerShipUseCaseImpl.class);

    private final BunkeringOperationQueryPort bunkeringOperationQueryPort;
    private final BunkeringOperationService bunkeringOperationService;
    private final LoadShippingOrderPort loadShippingOrderPort;
    private final UpdateShippingOrderPort updateShippingOrderPort;
    private final Clock clock;

    public BunkerShipUseCaseImpl(final BunkeringOperationQueryPort bunkeringOperationQueryPort,
                                 final LoadShippingOrderPort loadShippingOrderPort,
                                 final UpdateShippingOrderPort updateShippingOrderPort,
                                 final Clock clock) {
        this.bunkeringOperationQueryPort = bunkeringOperationQueryPort;
        this.bunkeringOperationService = new BunkeringOperationService(); // Instantiating the domain service
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.updateShippingOrderPort = updateShippingOrderPort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public ShippingOrder bunkerShip(final BunkerShipCommand command) {
        final ShippingOrderId id = command.shippingOrderId();
        final UserRole userRole = command.userRole();
        final String roleDisplayName = userRole.getDisplayName();
        final LocalDate today = LocalDate.now(clock);
        final int dailyCount =
            bunkeringOperationQueryPort.countPerformedBunkeringOperationsByDate(today);

        final ShippingOrder shippingOrder = loadShippingOrderPort.loadById(id).orElseThrow(
            () -> ShippingOrderNotFoundException.forId(id)
        );
        // 6 BOs per-day rule only applies to the bunkering officer
        if (requiresLimitCheck(userRole)) {
            LOGGER.info("Validating bunkering limit for {}", roleDisplayName);
            bunkeringOperationService.validateBunkeringLimit(dailyCount);
        } else {
            LOGGER.info("Bunkering limit validation skipped for {}", roleDisplayName);
        }
        shippingOrder.performBunkering();
        updateShippingOrderPort.updateShippingOrder(shippingOrder);

        LOGGER.info("Bunkering Operation has been performed for Vessel: {}",
            shippingOrder.getVesselNumber()
        );
        return shippingOrder;
    }

    private static boolean requiresLimitCheck(final UserRole userRole) {
        return userRole == UserRole.BUNKERING_OFFICER;
    }
}
