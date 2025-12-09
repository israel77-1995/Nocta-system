package za.co.ccos.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaException;
import za.co.ccos.infra.llm.LlamaOptions;
import za.co.ccos.infra.llm.LlamaResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceService {
    
    private final LlamaAdapter llamaAdapter;
    
    public String validateCompliance(String soapJson, String icd10Json, String allergies) throws LlamaException {
        List<String> allergyList = new java.util.ArrayList<>();
        if (allergies != null && !allergies.isEmpty()) {
            allergyList.add(allergies);
        }
        return validateCompliance(soapJson, icd10Json, allergyList);
    }
    
    public String validateCompliance(String soapJson, String icd10Json, List<String> allergies) throws LlamaException {
        try {
            String promptTemplate = loadPromptTemplate("prompts/compliance.prompt.txt");
            
            String prompt = promptTemplate
                    .replace("<<SOAP_JSON>>", soapJson != null ? soapJson : "{}")
                    .replace("<<ICD10_JSON>>", icd10Json != null ? icd10Json : "[]")
                    .replace("<<PATIENT_ALLERGIES>>", allergies != null && !allergies.isEmpty() ? String.join(", ", allergies) : "None");
            
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.1)
                    .maxTokens(1024)
                    .build();
            
            log.info("Running compliance agent");
            LlamaResponse response = llamaAdapter.runPrompt(prompt, options);
            
            return extractJsonFromResponse(response.getContent());
            
        } catch (IOException e) {
            throw new LlamaException("Failed to validate compliance", e);
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
