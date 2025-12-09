package za.co.ccos.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "generated_notes")
@Data
public class GeneratedNote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private UUID consultationId;
    
    @Column(columnDefinition = "TEXT")
    private String soapSubjective;
    
    @Column(columnDefinition = "TEXT")
    private String soapObjective;
    
    @Column(columnDefinition = "TEXT")
    private String soapAssessment;
    
    @Column(columnDefinition = "TEXT")
    private String soapPlan;
    
    @Column(name = "icd10_codes", columnDefinition = "TEXT")
    private String icd10Codes;
    
    @Column(columnDefinition = "TEXT")
    private String suggestedActions;
    
    @Column(columnDefinition = "TEXT")
    private String patientSummary;
    
    private Double confidence;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private UUID createdBy;
}
