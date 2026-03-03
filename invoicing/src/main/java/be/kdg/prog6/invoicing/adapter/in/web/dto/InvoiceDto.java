package be.kdg.prog6.invoicing.adapter.in.web.dto;

import be.kdg.prog6.invoicing.domain.Invoice;

import java.time.LocalDate;
import java.util.List;

public record InvoiceDto(
    String invoiceId,
    String customerId,
    LocalDate draftedDate,
    LocalDate sentDate,
    String status,
    List<InvoiceLineDto> invoiceLines,
    MoneyDto total
) {
    public static InvoiceDto fromDomain(final Invoice invoice) {
        return new InvoiceDto(
            invoice.getInvoiceId().id().toString(),
            invoice.getCustomerId().id().toString(),
            invoice.getDraftedDate(),
            invoice.getSentDate(),
            invoice.getStatus().name(),
            invoice.getInvoiceLines().stream().map(InvoiceLineDto::fromDomain).toList(),
            MoneyDto.of(invoice.calculateTotalAmount())
        );
    }
}