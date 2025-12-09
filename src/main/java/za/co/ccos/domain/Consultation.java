package za.co.ccos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Consultation entity representing a clinical consultation
 */
@Data
@Entity
@Table(name = "consultations")
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private UUID patientId;
    
    @Column(nullable = false)
    private UUID clinicianId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationState state;
    
    @Column(columnDefinition = "TEXT")
    private String audioTranscript;
    
    @Column(columnDefinition = "TEXT")
    private String rawTranscript;
    
    @Column(columnDefinition = "TEXT")
    private String structuredData;
    
    private String audioUrl;
    
    private String errorMessage;
    
    private UUID generatedNoteId;
    
    @Embedded
    private VitalSigns vitalSigns;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getTimestamp() {
        return this.createdAt;
    }
}
