package be.kdg.prog6.waterside.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class BunkeringOperation {
    private final BunkeringOperationId id;
    private final LocalDateTime queuedAt;
    private LocalDateTime performedAt;
    private BunkeringOperationStatus status;

    public BunkeringOperation() {
        this.id = BunkeringOperationId.of(UUID.randomUUID());
        this.queuedAt = LocalDateTime.now();
        this.status = BunkeringOperationStatus.QUEUED;
        this.performedAt = null;
    }

    public BunkeringOperation(final BunkeringOperationId id, final LocalDateTime queuedAt, final LocalDateTime performedAt, final BunkeringOperationStatus status) {
        this.id = id;
        this.queuedAt = queuedAt;
        this.performedAt = performedAt;
        this.status = status;
    }

    public boolean isQueued() {
        return this.status == BunkeringOperationStatus.QUEUED;
    }

    void complete() {
        setPerformedAt(LocalDateTime.now());
        setStatus(BunkeringOperationStatus.COMPLETED);
    }

    public boolean isCompleted() {
        return this.status == BunkeringOperationStatus.COMPLETED;
    }

    public BunkeringOperationId getId() {
        return id;
    }

    public LocalDateTime getQueuedAt() {
        return queuedAt;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    private void setPerformedAt(final LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public BunkeringOperationStatus getStatus() {
        return status;
    }

    private void setStatus(final BunkeringOperationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BunkeringOperation{queuedAt=%s, performedAt=%s, status=%s}".formatted(
            queuedAt, performedAt, status
        );
    }
}
