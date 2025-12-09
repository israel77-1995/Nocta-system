package za.co.ccos.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.ccos.domain.*;
import za.co.ccos.infra.persistence.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationOrchestrator {
    
    private final PerceptionService perceptionService;
    private final DocumentationService documentationService;
    private final CoordinationService coordinationService;
    private final ComplianceService complianceService;
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final GeneratedNoteRepository generatedNoteRepository;
    private final ObjectMapper objectMapper;
    
    @Async
    public void processConsultation(UUID consultationId) {
        try {
            log.info("Starting async processing of consultation {}", consultationId);
            
            // Fetch consultation
            Consultation consultation = consultationRepository.findById(consultationId)
                    .orElseThrow(() -> new RuntimeException("Consultation not found"));
            
            // Update state
            updateConsultationState(consultationId, ConsultationState.PROCESSING);
            
            Patient patient = patientRepository.findById(consultation.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            
            log.info("Processing consultation {} for patient {}", consultationId, patient.getId());
            
            // Step 1: Perception - extract structured facts
            JsonNode structuredFacts = perceptionService.extractStructuredFacts(
                    consultation.getRawTranscript(), patient);
            String structuredJson = objectMapper.writeValueAsString(structuredFacts);
            
            // Step 2: Documentation - generate SOAP note
            GeneratedNote note = documentationService.generateDocumentation(
                    structuredJson, 
                    consultation.getRawTranscript(),
                    consultationId,
                    consultation.getClinicianId());
            
            // Step 3: Coordination - generate actions
            String soapJson = objectMapper.writeValueAsString(objectMapper.createObjectNode()
                    .put("subjective", note.getSoapSubjective())
                    .put("objective", note.getSoapObjective())
                    .put("assessment", note.getSoapAssessment())
                    .put("plan", note.getSoapPlan()));
            
            String actions = coordinationService.generateActions(structuredJson, soapJson);
            note.setSuggestedActions(actions);
            
            // Step 4: Compliance - validate
            String complianceResult = complianceService.validateCompliance(
                    soapJson, 
                    note.getIcd10Codes(),
                    patient.getAllergies());
            
            log.info("Compliance check result: {}", complianceResult);
            
            // Save note with transaction
            saveNoteAndUpdateConsultation(note, consultationId);
            
            log.info("Consultation {} processed successfully", consultationId);
            
        } catch (Exception e) {
            log.error("Error processing consultation {}", consultationId, e);
            updateConsultationError(consultationId, e.getMessage());
        }
    }
    
    @Transactional
    private void saveNoteAndUpdateConsultation(GeneratedNote note, UUID consultationId) {
        GeneratedNote savedNote = generatedNoteRepository.save(note);
        
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        consultation.setGeneratedNoteId(savedNote.getId());
        consultation.setState(ConsultationState.READY);
        consultationRepository.save(consultation);
    }
    
    @Transactional
    private void updateConsultationState(UUID consultationId, ConsultationState state) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        consultation.setState(state);
        consultationRepository.save(consultation);
    }
    
    @Transactional
    private void updateConsultationError(UUID consultationId, String errorMessage) {
        consultationRepository.findById(consultationId).ifPresent(c -> {
            c.setState(ConsultationState.ERROR);
            c.setErrorMessage(errorMessage);
            consultationRepository.save(c);
        });
    }
}
