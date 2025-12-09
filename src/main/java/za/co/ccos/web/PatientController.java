package za.co.ccos.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.ccos.app.PatientSummaryService;
import za.co.ccos.domain.Patient;
import za.co.ccos.infra.persistence.PatientRepository;
import za.co.ccos.web.dto.PatientResponse;
import za.co.ccos.web.dto.PatientSummaryResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {
    
    private final PatientRepository patientRepository;
    private final PatientSummaryService summaryService;
    
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        log.info("Fetching all patients");
        
        List<PatientResponse> patients = patientRepository.findAll().stream()
                .filter(p -> !p.getId().equals(UUID.fromString("550e8400-e29b-41d4-a716-446655440099"))) // Exclude clinician
                .map(p -> PatientResponse.builder()
                        .id(p.getId())
                        .firstName(p.getFirstName())
                        .lastName(p.getLastName())
                        .dob(p.getDob())
                        .medicalRecordNumber(p.getMedicalRecordNumber())
                        .allergies(p.getAllergies())
                        .chronicConditions(p.getChronicConditions())
                        .build())
                .toList();
        
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable UUID id) {
        log.info("Fetching patient {}", id);
        
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        PatientResponse response = PatientResponse.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .dob(p.getDob())
                .medicalRecordNumber(p.getMedicalRecordNumber())
                .allergies(p.getAllergies())
                .chronicConditions(p.getChronicConditions())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/summary")
    public ResponseEntity<PatientSummaryResponse> getPatientSummary(@PathVariable UUID id) {
        log.info("Generating AI summary for patient {}", id);
        
        String summary = summaryService.generatePatientSummary(id);
        
        return ResponseEntity.ok(new PatientSummaryResponse(id, summary));
    }
}
