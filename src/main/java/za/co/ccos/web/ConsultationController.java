package za.co.ccos.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.ccos.app.ConsultationOrchestrator;
import za.co.ccos.domain.*;
import za.co.ccos.infra.persistence.*;
import za.co.ccos.web.dto.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
@Slf4j
public class ConsultationController {
    
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final GeneratedNoteRepository generatedNoteRepository;
    private final ConsultationOrchestrator orchestrator;
    
    @PostMapping("/upload-audio")
    public ResponseEntity<UploadResponse> uploadAudio(@RequestBody UploadRequest request) {
        log.info("Received consultation upload for patient {}", request.getPatientId());
        
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Consultation consultation = new Consultation();
        consultation.setPatientId(patient.getId());
        consultation.setClinicianId(request.getClinicianId());
        consultation.setRawTranscript(request.getRawTranscript());
        consultation.setAudioUrl(request.getAudioUrl());
        consultation.setVitalSigns(request.getVitalSigns());
        consultation.setState(ConsultationState.QUEUED);
        
        Consultation saved = consultationRepository.save(consultation);
        
        // Trigger async processing
        orchestrator.processConsultation(saved.getId());
        
        return ResponseEntity.ok(new UploadResponse(saved.getId(), saved.getState()));
    }
    
    @GetMapping("/patient/{patientId}/history")
    public ResponseEntity<List<ConsultationDetailResponse>> getPatientHistory(@PathVariable UUID patientId) {
        log.info("Fetching consultation history for patient {}", patientId);
        
        List<Consultation> consultations = consultationRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        
        List<ConsultationDetailResponse> responses = consultations.stream()
                .map(c -> {
                    ConsultationDetailResponse response = new ConsultationDetailResponse();
                    response.setId(c.getId());
                    response.setPatientId(c.getPatientId());
                    response.setClinicianId(c.getClinicianId());
                    response.setTimestamp(c.getTimestamp());
                    response.setRawTranscript(c.getRawTranscript());
                    response.setState(c.getState());
                    response.setVitalSigns(c.getVitalSigns());
                    
                    if (c.getGeneratedNoteId() != null) {
                        generatedNoteRepository.findById(c.getGeneratedNoteId())
                                .ifPresent(response::setGeneratedNote);
                    }
                    
                    return response;
                })
                .toList();
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}/status")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable UUID id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        
        return ResponseEntity.ok(new StatusResponse(
                consultation.getId(),
                consultation.getState(),
                consultation.getErrorMessage()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDetailResponse> getConsultation(@PathVariable UUID id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        
        ConsultationDetailResponse response = new ConsultationDetailResponse();
        response.setId(consultation.getId());
        response.setPatientId(consultation.getPatientId());
        response.setClinicianId(consultation.getClinicianId());
        response.setTimestamp(consultation.getTimestamp());
        response.setRawTranscript(consultation.getRawTranscript());
        response.setState(consultation.getState());
        
        if (consultation.getGeneratedNoteId() != null) {
            GeneratedNote note = generatedNoteRepository.findById(consultation.getGeneratedNoteId())
                    .orElse(null);
            response.setGeneratedNote(note);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApprovalResponse> approveConsultation(
            @PathVariable UUID id,
            @RequestBody ApprovalRequest request) {
        
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        
        if (request.isApprove()) {
            consultation.setState(ConsultationState.APPROVED);
            consultationRepository.save(consultation);
            
            log.info("Consultation {} approved by clinician {}", id, request.getClinicianId());
            
            // Simulate EHR sync
            boolean syncSuccess = simulateEhrSync(consultation);
            if (syncSuccess) {
                consultation.setState(ConsultationState.SYNCED);
                consultationRepository.save(consultation);
            }
            
            return ResponseEntity.ok(new ApprovalResponse(true, "Consultation approved and synced"));
        } else {
            return ResponseEntity.ok(new ApprovalResponse(false, "Consultation not approved"));
        }
    }
    
    private boolean simulateEhrSync(Consultation consultation) {
        log.info("Simulating FHIR sync for consultation {}", consultation.getId());
        // Simulate successful sync
        return true;
    }
}
