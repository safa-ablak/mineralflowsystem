package be.kdg.prog6.warehousing.domain.storage;

/**
 * Defines the physical boundaries of a site layout.
 * <p>
 * Bounds are expressed using easting and northing coordinates
 * relative to the site origin.
 */
public record SiteBounds(
    double minEasting,
    double maxEasting,
    double minNorthing,
    double maxNorthing
) {
    public boolean covers(final SiteLocation location, final WarehouseFootprint footprint) {
        return location.easting() >= minEasting
            && (location.easting() + footprint.width()) <= maxEasting
            && location.northing() >= minNorthing
            && (location.northing() + footprint.height()) <= maxNorthing;
    }
}
