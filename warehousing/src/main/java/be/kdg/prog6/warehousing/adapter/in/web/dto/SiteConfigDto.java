package be.kdg.prog6.warehousing.adapter.in.web.dto;

public record SiteConfigDto(
    double minEasting,
    double maxEasting,
    double minNorthing,
    double maxNorthing,
    double warehouseWidth,
    double warehouseHeight
) {
}