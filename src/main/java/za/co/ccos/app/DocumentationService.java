package za.co.ccos.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import za.co.ccos.domain.GeneratedNote;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaException;
import za.co.ccos.infra.llm.LlamaOptions;
import za.co.ccos.infra.llm.LlamaResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentationService {
    
    private final LlamaAdapter llamaAdapter;
    private final ObjectMapper objectMapper;
    
    public GeneratedNote generateDocumentation(String structuredJson, String transcript, UUID consultationId, UUID clinicianId) throws LlamaException {
        try {
            String promptTemplate = loadPromptTemplate("prompts/documentation.prompt.txt");
            
            String prompt = promptTemplate
                    .replace("<<STRUCTURED_JSON>>", structuredJson)
                    .replace("<<TRANSCRIPT>>", transcript);
            
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.1)
                    .maxTokens(2048)
                    .build();
            
            log.info("Running documentation agent for consultation {}", consultationId);
            LlamaResponse response = llamaAdapter.runPrompt(prompt, options);
            
            String content = extractJsonFromResponse(response.getContent());
            JsonNode docNode = objectMapper.readTree(content);
            
            GeneratedNote note = new GeneratedNote();
            note.setConsultationId(consultationId);
            note.setCreatedBy(clinicianId);
            
            if (docNode.has("soap")) {
                JsonNode soap = docNode.get("soap");
                note.setSoapSubjective(soap.has("subjective") ? soap.get("subjective").asText() : "");
                note.setSoapObjective(soap.has("objective") ? soap.get("objective").asText() : "");
                note.setSoapAssessment(soap.has("assessment") ? soap.get("assessment").asText() : "");
                note.setSoapPlan(soap.has("plan") ? soap.get("plan").asText() : "");
            }
            
            if (docNode.has("patient_summary")) {
                note.setPatientSummary(docNode.get("patient_summary").asText());
            }
            
            if (docNode.has("icd10_suggestions")) {
                note.setIcd10Codes(docNode.get("icd10_suggestions").toString());
            }
            
            if (docNode.has("confidence")) {
                note.setConfidence(docNode.get("confidence").asDouble());
            }
            
            return note;
            
        } catch (IOException e) {
            throw new LlamaException("Failed to generate documentation", e);
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
