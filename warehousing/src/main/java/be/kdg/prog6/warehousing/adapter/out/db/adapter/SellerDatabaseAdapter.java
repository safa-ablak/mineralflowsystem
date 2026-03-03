package be.kdg.prog6.warehousing.adapter.out.db.adapter;

import be.kdg.prog6.warehousing.adapter.out.db.entity.SellerJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.db.repository.SellerJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.value.AddressEmbeddable;
import be.kdg.prog6.warehousing.domain.Address;
import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SellerDatabaseAdapter implements LoadSellerPort {
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
}
