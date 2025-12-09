package za.co.ccos.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.ccos.infra.llm.GroqVisionAdapter;
import za.co.ccos.web.dto.ImageAnalysisRequest;
import za.co.ccos.web.dto.ImageAnalysisResponse;

@RestController
@RequestMapping("/api/v1/image-analysis")
@RequiredArgsConstructor
@Slf4j
public class ImageAnalysisController {
    
    private final GroqVisionAdapter visionAdapter;
    
    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisResponse> analyzeImage(@RequestBody ImageAnalysisRequest request) {
        log.info("Received image analysis request");
        
        try {
            String prompt = buildPrompt(request.getContext());
            String analysis = visionAdapter.analyzeImage(request.getBase64Image(), prompt);
            
            return ResponseEntity.ok(new ImageAnalysisResponse(true, analysis, null));
            
        } catch (Exception e) {
            log.error("Image analysis failed", e);
            return ResponseEntity.ok(new ImageAnalysisResponse(
                false, 
                null, 
                "Image analysis failed: " + e.getMessage()
            ));
        }
    }
    
    private String buildPrompt(String context) {
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
        prompt.append("4. RECOMMENDED ACTIONS: Next steps\n");
        prompt.append("5. URGENCY LEVEL: Routine / Urgent / Emergency\n\n");
        prompt.append("Be specific and actionable.\n");
        
        return prompt.toString();
    }
}
