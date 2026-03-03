package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.Buyer;
import be.kdg.prog6.warehousing.domain.BuyerId;

import java.util.Optional;

public interface LoadBuyerPort {
    boolean existsById(BuyerId id);

    Optional<Buyer> loadBuyerById(BuyerId id);
}
