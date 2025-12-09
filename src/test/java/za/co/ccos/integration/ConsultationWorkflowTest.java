package za.co.ccos.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import za.co.ccos.app.ConsultationOrchestrator;
import za.co.ccos.domain.*;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaResponse;
import za.co.ccos.infra.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ConsultationWorkflowTest {
    
    @Autowired
    private ConsultationRepository consultationRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private GeneratedNoteRepository generatedNoteRepository;
    
    @Autowired
    private ConsultationOrchestrator orchestrator;
    
    @MockBean
    private LlamaAdapter llamaAdapter;
    
    @Test
    void testFullConsultationWorkflow() throws Exception {
        String perceptionResponse = "{\"chief_complaint\":\"headache\",\"symptoms\":[{\"name\":\"headache\",\"onset\":\"3 days\",\"severity\":\"severe\"}],\"duration\":\"3 days\",\"vitals\":{\"bp\":\"140/90\"},\"medications_reported\":[],\"possible_differentials\":[\"migraine\"],\"red_flags\":[],\"missing_questions\":[]}";
        
        String documentationResponse = "{\"soap\":{\"subjective\":\"Patient reports severe headache for 3 days\",\"objective\":\"BP 140/90\",\"assessment\":\"Likely migraine\",\"plan\":\"Rest, hydration, follow-up in 1 week\"},\"patient_summary\":\"Patient with severe headache\",\"icd10_suggestions\":[{\"code\":\"G43.0\",\"desc\":\"Migraine without aura\",\"confidence\":0.9}],\"confidence\":0.85}";
        
        String coordinationResponse = "{\"actions\":[{\"id\":\"a1\",\"type\":\"PRESCRIPTION\",\"drug\":{\"name\":\"Ibuprofen\",\"dose\":\"400mg\"}}],\"notes\":\"Follow up\"}";
        
        String complianceResponse = "{\"issues\":[],\"suggested_edits\":[],\"compliance_ok\":true}";
        
        when(llamaAdapter.runPrompt(any(), any()))
                .thenReturn(new LlamaResponse(perceptionResponse))
                .thenReturn(new LlamaResponse(documentationResponse))
                .thenReturn(new LlamaResponse(coordinationResponse))
                .thenReturn(new LlamaResponse(complianceResponse));
        
        Patient patient = new Patient();
        patient.setFirstName("Test");
        patient.setLastName("Patient");
        patient.setDob(LocalDate.of(1980, 1, 1));
        patient = patientRepository.save(patient);
        
        Consultation consultation = new Consultation();
        consultation.setPatientId(patient.getId());
        consultation.setClinicianId(UUID.randomUUID());
        consultation.setRawTranscript("Patient reports severe headache for 3 days");
        consultation.setState(ConsultationState.QUEUED);
        consultation = consultationRepository.save(consultation);
        
        orchestrator.processConsultation(consultation.getId());
        
        Thread.sleep(2000);
        
        Consultation processed = consultationRepository.findById(consultation.getId()).orElseThrow();
        assertEquals(ConsultationState.READY, processed.getState());
        assertNotNull(processed.getGeneratedNoteId());
        
        GeneratedNote note = generatedNoteRepository.findById(processed.getGeneratedNoteId()).orElseThrow();
        assertNotNull(note.getSoapAssessment());
        assertTrue(note.getSoapAssessment().contains("migraine"));
        assertNotNull(note.getIcd10Codes());
        assertNotNull(note.getSuggestedActions());
    }
}
