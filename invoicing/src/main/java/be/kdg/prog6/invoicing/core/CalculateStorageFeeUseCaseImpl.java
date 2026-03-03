package be.kdg.prog6.invoicing.core;

import be.kdg.prog6.common.event.warehousing.StorageEntry;
import be.kdg.prog6.common.event.warehousing.WarehouseStorageReport;
import be.kdg.prog6.invoicing.domain.CustomerId;
import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.domain.exception.RawMaterialNotFoundException;
import be.kdg.prog6.invoicing.port.in.command.CalculateStorageFeeCommand;
import be.kdg.prog6.invoicing.port.in.usecase.CalculateStorageFeeUseCase;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;
import be.kdg.prog6.invoicing.port.out.UpdateInvoicePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculateStorageFeeUseCaseImpl implements CalculateStorageFeeUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateStorageFeeUseCaseImpl.class);

    private final LoadRawMaterialPort loadRawMaterialPort;
    private final LoadInvoicePort loadInvoicePort;
    private final UpdateInvoicePort updateInvoicePort;

    public CalculateStorageFeeUseCaseImpl(final LoadRawMaterialPort loadRawMaterialPort,
                                          final LoadInvoicePort loadInvoicePort,
                                          final UpdateInvoicePort updateInvoicePort) {
        this.loadRawMaterialPort = loadRawMaterialPort;
        this.loadInvoicePort = loadInvoicePort;
        this.updateInvoicePort = updateInvoicePort;
    }

    @Override
    @Transactional
    public void calculateStorageFee(final CalculateStorageFeeCommand command) {
        final CustomerId customerId = command.customerId();
        final List<WarehouseStorageReport> warehouseStorageReports = command.warehouseStorageReports();

        final Invoice draftInvoice = loadInvoicePort.loadDraftInvoiceByCustomerId(customerId);
        LOGGER.info("Calculating storage fee for Customer {}", customerId.id());

        for (WarehouseStorageReport report : warehouseStorageReports) {
            final String name = report.rawMaterial();
            final RawMaterial rawMaterial = loadRawMaterialPort.loadRawMaterialByName(name).orElseThrow(
                () -> RawMaterialNotFoundException.forName(name)
            );
            final List<StorageEntry> storageEntries = report.storageEntries();
            /// Could also be done by aggregating all reports into one
            /// before adding the fee (as a single invoice line).
            draftInvoice.addStorageCostFee(rawMaterial, storageEntries);
        }
        updateInvoicePort.updateInvoice(draftInvoice);
    }
}
