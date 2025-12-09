package za.co.ccos.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.ccos.domain.Consultation;
import za.co.ccos.domain.Patient;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaOptions;
import za.co.ccos.infra.persistence.ConsultationRepository;
import za.co.ccos.infra.persistence.PatientRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientSummaryService {
    
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final LlamaAdapter llamaAdapter;
    
    public String generatePatientSummary(UUID patientId) {
        log.info("Generating AI summary for patient {}", patientId);
        
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        List<Consultation> history = consultationRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        
        if (history.isEmpty()) {
            return generateFirstVisitSummary(patient);
        }
        
        String prompt = buildSummaryPrompt(patient, history);
        
        try {
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.3)
                    .maxTokens(400)
                    .build();
            
            String summary = llamaAdapter.runPrompt(prompt, options).getContent();
            log.info("Generated patient summary successfully");
            return summary;
            
        } catch (Exception e) {
            log.error("Failed to generate patient summary", e);
            return generateFallbackSummary(patient, history);
        }
    }
    
    private String buildSummaryPrompt(Patient patient, List<Consultation> history) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a clinical AI assistant. Analyze this patient's medical history and provide a concise summary for the clinician.\n\n");
        
        prompt.append("PATIENT INFORMATION:\n");
        prompt.append("Name: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n");
        prompt.append("Age: ").append(calculateAge(patient)).append(" years\n");
        prompt.append("Allergies: ").append(patient.getAllergies() != null && !patient.getAllergies().isEmpty() 
                ? String.join(", ", patient.getAllergies()) : "None").append("\n");
        prompt.append("Chronic Conditions: ").append(patient.getChronicConditions() != null && !patient.getChronicConditions().isEmpty()
                ? String.join(", ", patient.getChronicConditions()) : "None").append("\n\n");
        
        prompt.append("RECENT CONSULTATION HISTORY (").append(Math.min(5, history.size())).append(" most recent):\n\n");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        int count = 0;
        for (Consultation c : history) {
            if (count >= 5) break;
            prompt.append("Visit ").append(count + 1).append(" (").append(c.getCreatedAt().format(formatter)).append("):\n");
            
            if (c.getVitalSigns() != null) {
                prompt.append("Vitals: ");
                if (c.getVitalSigns().getBloodPressure() != null) 
                    prompt.append("BP ").append(c.getVitalSigns().getBloodPressure()).append(", ");
                if (c.getVitalSigns().getHeartRate() != null)
                    prompt.append("HR ").append(c.getVitalSigns().getHeartRate()).append(", ");
                if (c.getVitalSigns().getOxygenSaturation() != null)
                    prompt.append("O2 ").append(c.getVitalSigns().getOxygenSaturation()).append("%");
                prompt.append("\n");
            }
            
            String transcript = c.getRawTranscript();
            if (transcript != null && transcript.length() > 200) {
                transcript = transcript.substring(0, 200) + "...";
            }
            prompt.append("Notes: ").append(transcript).append("\n\n");
            count++;
        }
        
        prompt.append("\nTASK: Provide a brief clinical summary (3-4 sentences) highlighting:\n");
        prompt.append("1. Key medical issues and trends\n");
        prompt.append("2. Important alerts (allergies, recent changes)\n");
        prompt.append("3. Recommended focus areas for today's visit\n\n");
        prompt.append("Keep it concise and actionable for the clinician.\n");
        
        return prompt.toString();
    }
    
    private String generateFirstVisitSummary(Patient patient) {
        StringBuilder summary = new StringBuilder();
        summary.append("🆕 FIRST VISIT - New Patient\n\n");
        summary.append("📋 Patient: ").append(patient.getFirstName()).append(" ").append(patient.getLastName())
                .append(", ").append(calculateAge(patient)).append(" years old\n\n");
        
        if (patient.getAllergies() != null && !patient.getAllergies().isEmpty()) {
            summary.append("⚠️ ALLERGIES: ").append(String.join(", ", patient.getAllergies())).append("\n\n");
        }
        
        if (patient.getChronicConditions() != null && !patient.getChronicConditions().isEmpty()) {
            summary.append("💊 CHRONIC CONDITIONS: ").append(String.join(", ", patient.getChronicConditions())).append("\n\n");
        }
        
        summary.append("📝 RECOMMENDATION: Conduct comprehensive initial assessment including full medical history, current medications, and baseline vitals.");
        
        return summary.toString();
    }
    
    private String generateFallbackSummary(Patient patient, List<Consultation> history) {
        StringBuilder summary = new StringBuilder();
        summary.append("📊 PATIENT SUMMARY\n\n");
        summary.append("Patient: ").append(patient.getFirstName()).append(" ").append(patient.getLastName())
                .append(", ").append(calculateAge(patient)).append(" years\n");
        summary.append("Total Visits: ").append(history.size()).append("\n");
        
        if (patient.getAllergies() != null && !patient.getAllergies().isEmpty()) {
            summary.append("⚠️ Allergies: ").append(String.join(", ", patient.getAllergies())).append("\n");
        }
        
        if (patient.getChronicConditions() != null && !patient.getChronicConditions().isEmpty()) {
            summary.append("💊 Conditions: ").append(String.join(", ", patient.getChronicConditions())).append("\n");
        }
        
        if (!history.isEmpty()) {
            Consultation last = history.get(0);
            summary.append("\nLast Visit: ").append(last.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        }
        
        return summary.toString();
    }
    
    private int calculateAge(Patient patient) {
        if (patient.getDob() == null) return 0;
        return java.time.Period.between(patient.getDob(), java.time.LocalDate.now()).getYears();
    }
}
