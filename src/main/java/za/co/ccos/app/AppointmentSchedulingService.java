package za.co.ccos.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.ccos.domain.Consultation;
import za.co.ccos.domain.GeneratedNote;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentSchedulingService {
    
    private final LlamaAdapter llamaAdapter;
    
    public String suggestNextAppointment(Consultation consultation, GeneratedNote note) {
        log.info("Generating appointment recommendation for consultation {}", consultation.getId());
        
        String prompt = buildSchedulingPrompt(consultation, note);
        
        try {
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.3)
                    .maxTokens(300)
                    .build();
            
            String suggestion = llamaAdapter.runPrompt(prompt, options).getContent();
            log.info("Appointment recommendation generated");
            return suggestion;
            
        } catch (Exception e) {
            log.error("Failed to generate appointment recommendation", e);
            return generateDefaultRecommendation(note);
        }
    }
    
    private String buildSchedulingPrompt(Consultation consultation, GeneratedNote note) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a clinical scheduling assistant. Based on the consultation, recommend the next follow-up appointment.\n\n");
        
        prompt.append("ASSESSMENT:\n").append(note.getSoapAssessment()).append("\n\n");
        prompt.append("PLAN:\n").append(note.getSoapPlan()).append("\n\n");
        
        prompt.append("TASK: Recommend next appointment timing and provide brief rationale.\n\n");
        prompt.append("Consider:\n");
        prompt.append("- Condition severity and stability\n");
        prompt.append("- Medication changes requiring monitoring\n");
        prompt.append("- Lab results pending review\n");
        prompt.append("- Chronic disease management intervals\n");
        prompt.append("- Urgent vs routine follow-up needs\n\n");
        
        prompt.append("Return in this format:\n");
        prompt.append("TIMEFRAME: [1 week | 2 weeks | 1 month | 3 months | 6 months | As needed]\n");
        prompt.append("REASON: [Brief clinical rationale]\n");
        prompt.append("PRIORITY: [Urgent | Routine | Optional]\n");
        
        return prompt.toString();
    }
    
    private String generateDefaultRecommendation(GeneratedNote note) {
        return "TIMEFRAME: 2 weeks\nREASON: Standard follow-up to assess treatment response\nPRIORITY: Routine";
    }
}
