package za.co.ccos.app;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CoordinationService {
    
    private final LlamaAdapter llamaAdapter;
    @SuppressWarnings("unused")
    private final ObjectMapper objectMapper;
    
    public String generateActions(String structuredJson, String soapJson) throws LlamaException {
        try {
            String promptTemplate = loadPromptTemplate("prompts/coordination.prompt.txt");
            
            String prompt = promptTemplate
                    .replace("<<STRUCTURED_JSON>>", structuredJson)
                    .replace("<<SOAP_JSON>>", soapJson);
            
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.3)
                    .maxTokens(1024)
                    .build();
            
            log.info("Running coordination agent");
            LlamaResponse response = llamaAdapter.runPrompt(prompt, options);
            
            return extractJsonFromResponse(response.getContent());
            
        } catch (IOException e) {
            throw new LlamaException("Failed to generate coordination actions", e);
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
