package be.kdg.prog6.warehousing.adapter.in.web.controller;

import be.kdg.prog6.warehousing.adapter.in.web.dto.SiteConfigDto;
import be.kdg.prog6.warehousing.domain.storage.SiteBounds;
import be.kdg.prog6.warehousing.domain.storage.WarehouseFootprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;

@RestController
@RequestMapping("/warehousing/site")
public class WarehousingSiteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehousingSiteController.class);

    private final SiteBounds siteBounds;
    private final WarehouseFootprint warehouseFootprint;

    public WarehousingSiteController(final SiteBounds siteBounds, final WarehouseFootprint warehouseFootprint) {
        this.siteBounds = siteBounds;
        this.warehouseFootprint = warehouseFootprint;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to know the site configuration (boundaries and warehouse footprint),
     * so I can understand where warehouses can be placed and their dimensions.
     */
    @GetMapping("/config")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<SiteConfigDto> getSiteConfig(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing the Site Configuration");
        return ResponseEntity.ok(new SiteConfigDto(
            siteBounds.minEasting(),
            siteBounds.maxEasting(),
            siteBounds.minNorthing(),
            siteBounds.maxNorthing(),
            warehouseFootprint.width(),
            warehouseFootprint.height()
        ));
    }
}
