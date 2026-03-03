package be.kdg.prog6.waterside.domain;

import java.time.LocalDate;
import java.util.UUID;

public class InspectionOperation {
    /*
     * Attribute(s)
     */
    private final InspectionOperationId id;
    // There's no such rule regarding operation order between IO and BO
    // so just storing as LocalDate rather than LocalDateTime
    private LocalDate performedOn;
    private String inspectorSignature;
    private InspectionOperationStatus status;

    /*
     * Constructor(s)
     */
    public InspectionOperation() {
        this.id = InspectionOperationId.of(UUID.randomUUID());
        this.performedOn = null;
        this.inspectorSignature = null;
        this.status = InspectionOperationStatus.SCHEDULED;
    }

    public InspectionOperation(final InspectionOperationId id, final LocalDate performedOn, final String inspectorSignature, final InspectionOperationStatus status) {
        this.id = id;
        this.performedOn = performedOn;
        this.inspectorSignature = inspectorSignature;
        this.status = status;
    }

    /*
     * Method(s)
     */
    public boolean isScheduled() {
        return this.status == InspectionOperationStatus.SCHEDULED;
    }

    void complete(final String inspectorSignature) {
        setPerformedOn(LocalDate.now());
        setInspectorSignature(inspectorSignature);
        setStatus(InspectionOperationStatus.COMPLETED);
    }

    public boolean isCompleted() {
        return this.status == InspectionOperationStatus.COMPLETED;
    }

    /*
     * Getter(s)/Setter(s)
     */

    public InspectionOperationId getId() {
        return id;
    }

    public LocalDate getPerformedOn() {
        return performedOn;
    }

    private void setPerformedOn(final LocalDate performedOn) {
        this.performedOn = performedOn;
    }

    public String getInspectorSignature() {
        return inspectorSignature;
    }

    private void setInspectorSignature(final String inspectorSignature) {
        this.inspectorSignature = inspectorSignature;
    }

    public InspectionOperationStatus getStatus() {
        return status;
    }

    private void setStatus(final InspectionOperationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InspectionOperation{performedOn=%s, inspectorSignature=%s, status=%s}".formatted(
            performedOn, inspectorSignature, status
        );
    }
}

