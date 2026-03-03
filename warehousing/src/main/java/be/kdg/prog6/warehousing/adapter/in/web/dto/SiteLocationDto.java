package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.domain.storage.SiteLocation;

public record SiteLocationDto(double easting, double northing) {
    public static SiteLocationDto of(final SiteLocation siteLocation) {
        return new SiteLocationDto(siteLocation.easting(), siteLocation.northing());
    }
}
