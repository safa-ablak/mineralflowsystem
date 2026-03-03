package be.kdg.prog6.warehousing.domain.storage;

/**
 * Represents a physical position on a site layout.
 * <p>
 * The location is expressed using {@code easting} and {@code northing},
 * measured in <...> from the site's south-west origin.
 */
public record SiteLocation(double easting, double northing) {
    @Override
    public String toString() {
        return "easting=" + easting + ", northing=" + northing;
    }
}