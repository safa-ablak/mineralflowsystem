package be.kdg.prog6.warehousing.adapter.config.site;

import be.kdg.prog6.warehousing.domain.storage.SiteBounds;
import be.kdg.prog6.warehousing.domain.storage.WarehouseFootprint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SiteConfigurationProperties.class)
public class WarehousingSiteConfiguration {
    @Bean
    public SiteBounds siteBounds(final SiteConfigurationProperties props) {
        final var bounds = props.bounds();
        return new SiteBounds(
            bounds.minEasting(),
            bounds.maxEasting(),
            bounds.minNorthing(),
            bounds.maxNorthing()
        );
    }

    @Bean
    public WarehouseFootprint warehouseFootprint(final SiteConfigurationProperties props) {
        final var footprint = props.warehouseFootprint();
        return new WarehouseFootprint(footprint.width(), footprint.height());
    }
}
