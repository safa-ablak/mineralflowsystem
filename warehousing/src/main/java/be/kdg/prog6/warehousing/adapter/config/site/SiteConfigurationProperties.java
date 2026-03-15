package be.kdg.prog6.warehousing.adapter.config.site;

import be.kdg.prog6.warehousing.domain.storage.SiteBounds;
import be.kdg.prog6.warehousing.domain.storage.WarehouseFootprint;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kdg.site")
public record SiteConfigurationProperties(
    SiteBounds bounds,
    WarehouseFootprint warehouseFootprint
) {}