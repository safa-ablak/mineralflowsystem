package be.kdg.prog6.warehousing.adapter.out.persistence.adapter;

import be.kdg.prog6.warehousing.adapter.out.persistence.entity.SellerJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.persistence.repository.SellerJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.persistence.value.AddressEmbeddable;
import be.kdg.prog6.warehousing.domain.Address;
import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import be.kdg.prog6.warehousing.port.out.ProfilePicture;
import be.kdg.prog6.warehousing.port.out.SellerProfilePicturePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SellerDatabaseAdapter implements LoadSellerPort, SellerProfilePicturePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(SellerDatabaseAdapter.class);

    private final SellerJpaRepository sellerJpaRepository;

    public SellerDatabaseAdapter(final SellerJpaRepository sellerJpaRepository) {
        this.sellerJpaRepository = sellerJpaRepository;
    }

    @Override
    public boolean existsById(final SellerId id) {
        LOGGER.info("Checking if Seller with ID {} exists", id.id());
        final boolean exists = sellerJpaRepository.existsById(id.id());
        LOGGER.info("Seller with ID {} {}", id.id(), exists ? "exists" : "does not exist");
        return exists;
    }

    @Override
    public Optional<Seller> loadSellerById(final SellerId id) {
        LOGGER.info("Loading Seller with ID {}", id.id());
        return sellerJpaRepository.findById(id.id()).map(this::toSeller);
    }

    @Override
    public List<SellerId> loadAllSellerIds() {
        LOGGER.info("Loading all Seller IDs");
        return sellerJpaRepository.findAllIds().stream().map(SellerId::of).toList();
    }

    private Seller toSeller(final SellerJpaEntity sellerJpa) {
        return new Seller(
            SellerId.of(sellerJpa.getId()),
            sellerJpa.getName(),
            toAddress(sellerJpa.getAddress())
        );
    }

    private static Address toAddress(final AddressEmbeddable addressEmbeddable) {
        return new Address(
            addressEmbeddable.getStreetName(),
            addressEmbeddable.getStreetNumber(),
            addressEmbeddable.getCity(),
            addressEmbeddable.getCountry()
        );
    }

    @Override
    public Optional<ProfilePicture> loadProfilePicture(final SellerId sellerId) {
        LOGGER.info("Loading Profile Picture for Seller with ID {}", sellerId.id());
        final SellerJpaEntity sellerJpa = sellerJpaRepository.findById(sellerId.id()).orElseThrow();
        if (sellerJpa.hasProfilePicture()) {
            return Optional.of(new ProfilePicture(sellerJpa.getProfilePicture(), sellerJpa.getProfilePictureContentType()));
        }
        return Optional.empty();
    }

    @Override
    public void saveProfilePicture(final SellerId sellerId, final byte[] content, final String contentType) {
        LOGGER.info("Saving Profile Picture for Seller with ID {}", sellerId.id());
        final SellerJpaEntity sellerJpa = sellerJpaRepository.findById(sellerId.id()).orElseThrow();
        sellerJpa.setProfilePicture(content);
        sellerJpa.setProfilePictureContentType(contentType);
        sellerJpaRepository.save(sellerJpa);
    }

    @Override
    public void removeProfilePicture(final SellerId sellerId) {
        LOGGER.info("Removing Profile Picture for Seller with ID {}", sellerId.id());
        final SellerJpaEntity sellerJpa = sellerJpaRepository.findById(sellerId.id()).orElseThrow();
        sellerJpa.setProfilePicture(null);
        sellerJpa.setProfilePictureContentType(null);
        sellerJpaRepository.save(sellerJpa);
    }
}
