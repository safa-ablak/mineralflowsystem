package be.kdg.prog6.warehousing.adapter.out.persistence.adapter;

import be.kdg.prog6.warehousing.adapter.out.persistence.entity.BuyerJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.persistence.repository.BuyerJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.persistence.value.AddressEmbeddable;
import be.kdg.prog6.warehousing.domain.Address;
import be.kdg.prog6.warehousing.domain.Buyer;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.port.out.BuyerProfilePicturePort;
import be.kdg.prog6.warehousing.port.out.LoadBuyerPort;
import be.kdg.prog6.warehousing.port.out.ProfilePicture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BuyerDatabaseAdapter implements LoadBuyerPort, BuyerProfilePicturePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerDatabaseAdapter.class);

    private final BuyerJpaRepository buyerJpaRepository;

    public BuyerDatabaseAdapter(final BuyerJpaRepository buyerJpaRepository) {
        this.buyerJpaRepository = buyerJpaRepository;
    }

    @Override
    public boolean existsById(final BuyerId id) {
        LOGGER.info("Checking if Buyer with ID {} exists", id.id());
        final boolean exists = buyerJpaRepository.existsById(id.id());
        LOGGER.info("Buyer with ID {} {}", id.id(), exists ? "exists" : "does not exist");
        return exists;
    }

    @Override
    public Optional<Buyer> loadBuyerById(final BuyerId id) {
        LOGGER.info("Loading Buyer with ID {}", id.id());
        return buyerJpaRepository.findById(id.id()).map(this::toBuyer);
    }

    private Buyer toBuyer(final BuyerJpaEntity buyerJpa) {
        return new Buyer(
            BuyerId.of(buyerJpa.getId()),
            buyerJpa.getName(),
            toAddress(buyerJpa.getAddress())
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
    public Optional<ProfilePicture> loadProfilePicture(final BuyerId buyerId) {
        LOGGER.info("Loading Profile Picture for Buyer with ID {}", buyerId.id());
        final BuyerJpaEntity buyerJpa = buyerJpaRepository.findById(buyerId.id()).orElseThrow();
        if (buyerJpa.hasProfilePicture()) {
            return Optional.of(new ProfilePicture(buyerJpa.getProfilePicture(), buyerJpa.getProfilePictureContentType()));
        }
        return Optional.empty();
    }

    @Override
    public void saveProfilePicture(final BuyerId buyerId, final byte[] content, final String contentType) {
        LOGGER.info("Saving Profile Picture for Buyer with ID {}", buyerId.id());
        final BuyerJpaEntity buyerJpa = buyerJpaRepository.findById(buyerId.id()).orElseThrow();
        buyerJpa.setProfilePicture(content);
        buyerJpa.setProfilePictureContentType(contentType);
        buyerJpaRepository.save(buyerJpa);
    }

    @Override
    public void removeProfilePicture(final BuyerId buyerId) {
        LOGGER.info("Removing Profile Picture for Buyer with ID {}", buyerId.id());
        final BuyerJpaEntity buyerJpa = buyerJpaRepository.findById(buyerId.id()).orElseThrow();
        buyerJpa.setProfilePicture(null);
        buyerJpa.setProfilePictureContentType(null);
        buyerJpaRepository.save(buyerJpa);
    }
}
