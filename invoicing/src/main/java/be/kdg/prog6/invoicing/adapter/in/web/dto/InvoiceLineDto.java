package be.kdg.prog6.invoicing.adapter.in.web.dto;

import be.kdg.prog6.invoicing.domain.InvoiceLine;

public record InvoiceLineDto(
    MoneyDto amount, // nested amount field -> Either rename the field in MoneyDto or rename the parameter here
    String type
) {
    public static InvoiceLineDto fromDomain(final InvoiceLine invoiceLine) {
        return new InvoiceLineDto(
            MoneyDto.of(invoiceLine.getAmount()),
            invoiceLine.getType().name()
        );
    }
}
