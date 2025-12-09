package za.co.ccos.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.ccos.domain.Consultation;
import za.co.ccos.domain.GeneratedNote;
import za.co.ccos.domain.Patient;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaOptions;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientEmailService {
    
    private final LlamaAdapter llamaAdapter;
    
    public String generatePatientFriendlyExplanation(Patient patient, GeneratedNote note) {
        log.info("Generating patient-friendly explanation for {}", patient.getFirstName());
        
        String prompt = buildPatientExplanationPrompt(patient, note);
        
        try {
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.4)
                    .maxTokens(600)
                    .build();
            
            String explanation = llamaAdapter.runPrompt(prompt, options).getContent();
            log.info("Patient explanation generated");
            return explanation;
            
        } catch (Exception e) {
            log.error("Failed to generate patient explanation", e);
            return generateSimpleExplanation(note);
        }
    }
    
    private String buildPatientExplanationPrompt(Patient patient, GeneratedNote note) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a compassionate doctor explaining medical findings to a patient in simple, non-technical language.\n\n");
        
        prompt.append("PATIENT: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n\n");
        
        prompt.append("CLINICAL ASSESSMENT:\n").append(note.getSoapAssessment()).append("\n\n");
        prompt.append("TREATMENT PLAN:\n").append(note.getSoapPlan()).append("\n\n");
        
        prompt.append("TASK: Write a warm, clear email to the patient explaining:\n");
        prompt.append("1. What we found (in simple terms)\n");
        prompt.append("2. What it means for their health\n");
        prompt.append("3. What we're doing about it\n");
        prompt.append("4. What they need to do\n");
        prompt.append("5. When to follow up\n\n");
        
        prompt.append("GUIDELINES:\n");
        prompt.append("- Use everyday language (avoid medical jargon)\n");
        prompt.append("- Be reassuring but honest\n");
        prompt.append("- Keep it conversational and warm\n");
        prompt.append("- Use analogies if helpful\n");
        prompt.append("- Include specific action items\n");
        prompt.append("- End with encouragement\n\n");
        
        prompt.append("Format as a friendly email (no subject line needed).\n");
        
        return prompt.toString();
    }
    
    private String generateSimpleExplanation(GeneratedNote note) {
        return String.format(
            "Dear Patient,\n\n" +
            "Thank you for your visit today. Here's a summary of what we discussed:\n\n" +
            "WHAT WE FOUND:\n%s\n\n" +
            "NEXT STEPS:\n%s\n\n" +
            "Please follow the treatment plan we discussed. " +
            "If you have any questions or concerns, don't hesitate to contact us.\n\n" +
            "Take care,\nYour Healthcare Team",
            note.getSoapAssessment(),
            note.getSoapPlan()
        );
    }
    
    public void sendEmail(String patientEmail, String subject, String body) {
        // Simulate email sending
        log.info("📧 EMAIL SENT to {}", patientEmail);
        log.info("Subject: {}", subject);
        log.info("Body preview: {}", body.substring(0, Math.min(100, body.length())));
        
        // In production, integrate with SendGrid, AWS SES, or SMTP
        // For MVP, we log it and could display in UI
    }
}
