package za.co.ccos.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import za.co.ccos.domain.Patient;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaException;
import za.co.ccos.infra.llm.LlamaOptions;
import za.co.ccos.infra.llm.LlamaResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerceptionService {
    
    private final LlamaAdapter llamaAdapter;
    private final ObjectMapper objectMapper;
    
    public JsonNode extractStructuredFacts(String transcript, Patient patient) throws LlamaException {
        try {
            String promptTemplate = loadPromptTemplate("prompts/perception.prompt.txt");
            
            int age = patient.getDob() != null ? 
                    Period.between(patient.getDob(), LocalDate.now()).getYears() : 0;
            
            String prompt = promptTemplate
                    .replace("<<TRANSCRIPT>>", transcript)
                    .replace("<<PATIENT_NAME>>", patient.getFirstName() + " " + patient.getLastName())
                    .replace("<<AGE>>", String.valueOf(age))
                    .replace("<<GENDER>>", "Unknown")
                    .replace("<<ALLERGIES>>", String.join(", ", patient.getAllergies()))
                    .replace("<<CHRONIC_CONDITIONS>>", String.join(", ", patient.getChronicConditions()));
            
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.2)
                    .maxTokens(1024)
                    .build();
            
            log.info("Running perception agent for patient {}", patient.getId());
            LlamaResponse response = llamaAdapter.runPrompt(prompt, options);
            
            String content = extractJsonFromResponse(response.getContent());
            return objectMapper.readTree(content);
            
        } catch (IOException e) {
            throw new LlamaException("Failed to process perception", e);
        }
    }
    
    private String loadPromptTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
    
    private String extractJsonFromResponse(String response) {
        response = response.trim();
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }
        return response;
    }
}
