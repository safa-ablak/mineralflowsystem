package be.kdg.prog6.warehousing.adapter.out.db.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class SiteLocationEmbeddable {
    @Column(name = "site_easting")
    private double easting;

    @Column(name = "site_northing")
    private double northing;

    protected SiteLocationEmbeddable() {
    }

    public SiteLocationEmbeddable(final double easting, final double northing) {
        this.easting = easting;
        this.northing = northing;
    }

    public double getEasting() {
        return easting;
    }

    public double getNorthing() {
        return northing;
    }
}
