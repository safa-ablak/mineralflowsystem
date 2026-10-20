package be.kdg.prog6.warehousing.adapter.out.persistence.entity;

import be.kdg.prog6.warehousing.adapter.out.persistence.value.AddressEmbeddable;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(catalog = "warehousing", name = "sellers")
public class SellerJpaEntity {
    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private AddressEmbeddable address;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @Column(name = "profile_picture_content_type")
    private String profilePictureContentType;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public AddressEmbeddable getAddress() {
        return address;
    }

    public void setAddress(final AddressEmbeddable address) {
        this.address = address;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public boolean hasProfilePicture() {
        return profilePicture != null;
    }

    public void setProfilePicture(final byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureContentType() {
        return profilePictureContentType;
    }

    public void setProfilePictureContentType(final String profilePictureContentType) {
        this.profilePictureContentType = profilePictureContentType;
    }
}
