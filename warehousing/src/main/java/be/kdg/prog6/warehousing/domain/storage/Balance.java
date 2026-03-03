package be.kdg.prog6.warehousing.domain.storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Balance(LocalDateTime time, BigDecimal amount) {
    public static final Balance ORIGIN = new Balance(LocalDateTime.MIN, BigDecimal.ZERO);

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public String toString() {
        return "time=%s, amount=%s".formatted(time, amount);
    }
}
