package be.kdg.prog6.warehousing.adapter.config.site;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kdg.site")
public record SiteConfigurationProperties(
    Bounds bounds,
    WarehouseFootprintProperties warehouseFootprint
) {
    public record Bounds(
        double minEasting,
        double maxEasting,
        double minNorthing,
        double maxNorthing
    ) {}

    public record WarehouseFootprintProperties(
        double width,
        double height
    ) {}
}