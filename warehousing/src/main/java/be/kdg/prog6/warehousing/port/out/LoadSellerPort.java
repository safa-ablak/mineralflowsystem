package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;

import java.util.List;
import java.util.Optional;

public interface LoadSellerPort {
    boolean existsById(SellerId id);

    Optional<Seller> loadSellerById(SellerId id);

    List<SellerId> loadAllSellerIds();
}