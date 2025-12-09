package za.co.ccos.domain;

/**
 * States that a consultation can be in
 */
public enum ConsultationState {
    QUEUED,
    PROCESSING,
    READY,
    APPROVED,
    SYNCED,
    ERROR
}
