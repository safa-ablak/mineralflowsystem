package be.kdg.prog6.invoicing;

import be.kdg.prog6.invoicing.domain.CustomerId;
import be.kdg.prog6.invoicing.domain.InvoiceId;
import be.kdg.prog6.invoicing.domain.InvoiceLineId;
import be.kdg.prog6.invoicing.domain.RawMaterialId;

import java.time.LocalDate;
import java.util.UUID;

final class TestIds {
    public static final CustomerId CUSTOMER_ID = CustomerId.of(
        UUID.fromString("fc7a180d-1dcf-4a80-bf37-23e4fa69f9e4")
    );

    public static final InvoiceId INVOICE_ID = InvoiceId.of(
        UUID.fromString("d1c9e5b8-9c3e-4f0a-8c2b-1a2b3c4d5e6f")
    );
    public static final InvoiceLineId INVOICE_LINE_1_ID = InvoiceLineId.of(
        UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    );

    public static final String RAW_MATERIAL_NAME = "IRON_ORE";
    public static final RawMaterialId RAW_MATERIAL_ID = RawMaterialId.of(
        UUID.fromString("4f154dc1-af9e-4e80-9c61-9ba04fc20834")
    );

    public static final LocalDate
        INVOICE_DRAFTED_DATE = LocalDate.of(2025, 1, 1);
    public static final LocalDate
        INVOICE_SENT_DATE = INVOICE_DRAFTED_DATE.plusDays(1);

    private TestIds() {
        throw new AssertionError("Utility class");
    }
}
