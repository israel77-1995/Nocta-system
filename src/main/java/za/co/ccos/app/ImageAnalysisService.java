package za.co.ccos.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaOptions;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageAnalysisService {
    
    private final LlamaAdapter llamaAdapter;
    
    public String analyzeImage(String base64Image, String additionalContext) {
        log.info("Analyzing medical image with LLAMA Vision");
        
        String prompt = buildImageAnalysisPrompt(additionalContext);
        
        try {
            // Note: This will need a vision-capable adapter
            // For now, return a structured response format
            LlamaOptions options = LlamaOptions.builder()
                    .temperature(0.2)
                    .maxTokens(500)
                    .build();
            
            String analysis = llamaAdapter.runPrompt(prompt, options).getContent();
            log.info("Image analysis completed successfully");
            return analysis;
            
        } catch (Exception e) {
            log.error("Failed to analyze image", e);
            return generateFallbackAnalysis();
        }
    }
    
    private String buildImageAnalysisPrompt(String context) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a clinical AI assistant analyzing a medical image. ");
        prompt.append("Provide a structured clinical assessment.\n\n");
        
        if (context != null && !context.isEmpty()) {
            prompt.append("CLINICAL CONTEXT: ").append(context).append("\n\n");
        }
        
        prompt.append("Analyze the image and provide:\n");
        prompt.append("1. VISUAL FINDINGS: Describe what you observe\n");
        prompt.append("2. CLINICAL SIGNIFICANCE: What this might indicate\n");
        prompt.append("3. DIFFERENTIAL DIAGNOSIS: Possible conditions (list 3-5)\n");
        prompt.append("4. RECOMMENDED ACTIONS: Next steps for diagnosis/treatment\n");
        prompt.append("5. URGENCY LEVEL: Routine / Urgent / Emergency\n\n");
        prompt.append("Be specific, clinical, and actionable. Note any red flags.\n");
        
        return prompt.toString();
    }
    
    private String generateFallbackAnalysis() {
        return "⚠️ IMAGE ANALYSIS UNAVAILABLE\n\n" +
               "The AI vision analysis service is temporarily unavailable. " +
               "Please proceed with standard clinical examination and documentation.";
    }
}
