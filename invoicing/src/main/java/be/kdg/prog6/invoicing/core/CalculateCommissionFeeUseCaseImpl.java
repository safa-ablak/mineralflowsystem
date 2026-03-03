package be.kdg.prog6.invoicing.core;

import be.kdg.prog6.invoicing.domain.*;
import be.kdg.prog6.invoicing.domain.exception.RawMaterialNotFoundException;
import be.kdg.prog6.invoicing.domain.service.RawMaterialCostCalculator;
import be.kdg.prog6.invoicing.port.in.command.CalculateCommissionFeeCommand;
import be.kdg.prog6.invoicing.port.in.usecase.CalculateCommissionFeeUseCase;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;
import be.kdg.prog6.invoicing.port.out.LoadYearlyCommissionRatePort;
import be.kdg.prog6.invoicing.port.out.UpdateInvoicePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalculateCommissionFeeUseCaseImpl implements CalculateCommissionFeeUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateCommissionFeeUseCaseImpl.class);

    private final LoadRawMaterialPort loadRawMaterialPort;
    private final LoadInvoicePort loadInvoicePort;
    private final RawMaterialCostCalculator rawMaterialCostCalculator;
    private final LoadYearlyCommissionRatePort loadYearlyCommissionRatePort;
    private final UpdateInvoicePort updateInvoicePort;

    public CalculateCommissionFeeUseCaseImpl(final LoadRawMaterialPort loadRawMaterialPort,
                                             final LoadInvoicePort loadInvoicePort,
                                             final LoadYearlyCommissionRatePort loadYearlyCommissionRatePort,
                                             final UpdateInvoicePort updateInvoicePort) {
        this.loadRawMaterialPort = loadRawMaterialPort;
        this.loadInvoicePort = loadInvoicePort;
        this.rawMaterialCostCalculator = new RawMaterialCostCalculator();
        this.loadYearlyCommissionRatePort = loadYearlyCommissionRatePort;
        this.updateInvoicePort = updateInvoicePort;
    }

    @Override
    @Transactional
    public void calculateCommissionFee(final CalculateCommissionFeeCommand command) {
        final CustomerId customerId = command.customerId();
        final Invoice draftInvoice = loadInvoicePort.loadDraftInvoiceByCustomerId(customerId);

        final Map<RawMaterial, BigDecimal> rawMaterialToAmount = mapRawMaterials(command.rawMaterialToAmount());
        // (Total Raw Material costs are not invoiced to the customer)
        final Money totalRawMaterialCosts = rawMaterialCostCalculator.calculateTotalRawMaterialCosts(rawMaterialToAmount);

        final YearlyCommissionRate commissionRate = loadYearlyCommissionRatePort.loadCurrentYearRate();
        // Add commission fee to the invoice
        final InvoiceLine commissionFee = draftInvoice.addCommissionFee(totalRawMaterialCosts, commissionRate.rate());

        LOGGER.info(
            "Calculated Commission Fee for Customer {}: Total Raw Material Costs = {}, Commission Rate = {}%, Fee = {}",
            customerId.id(),
            totalRawMaterialCosts,
            commissionRate.asPercentage(),
            commissionFee.getAmount()
        );
        updateInvoicePort.updateInvoice(draftInvoice);
    }

    /**
     * Converts raw material names to a raw material-amount map using the LoadRawMaterialPort.
     */
    private Map<RawMaterial, BigDecimal> mapRawMaterials(final Map<String, BigDecimal> rawMaterialToAmount) {
        return rawMaterialToAmount.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> resolveRawMaterialOrThrow(entry.getKey()),
                Map.Entry::getValue
            ));
    }

    /**
     * Resolves a raw material from the database or throws an error.
     */
    private RawMaterial resolveRawMaterialOrThrow(final String name) {
        return loadRawMaterialPort.loadRawMaterialByName(name).orElseThrow(
            () -> RawMaterialNotFoundException.forName(name)
        );
    }
}
