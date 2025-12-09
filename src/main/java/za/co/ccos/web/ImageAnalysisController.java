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
            log.error("Image analysis failed: {}", e.getMessage());
            
            // Provide helpful template as fallback
            String template = generateDocumentationTemplate(request.getContext());
            return ResponseEntity.ok(new ImageAnalysisResponse(
                true, 
                template, 
                "AI vision unavailable - using documentation template. To enable AI analysis, add OPENROUTER_API_KEY to .env (see OPENROUTER_SETUP.md)"
            ));
        }
    }
    
    private String generateDocumentationTemplate(String context) {
        StringBuilder template = new StringBuilder();
        template.append("📸 IMAGE CAPTURED - Clinical Documentation\n\n");
        
        if (context != null && !context.isEmpty()) {
            template.append("CONTEXT: ").append(context).append("\n\n");
        }
        
        template.append("VISUAL FINDINGS:\n");
        template.append("• Location: [Describe location]\n");
        template.append("• Size: [Measure in cm]\n");
        template.append("• Appearance: [Color, texture, borders]\n");
        template.append("• Drainage: [Type/amount if present]\n");
        template.append("• Surrounding tissue: [Erythema, warmth, swelling]\n\n");
        
        template.append("ASSESSMENT:\n");
        template.append("• Clinical impression: [Your assessment]\n");
        template.append("• Differential diagnosis: [Possibilities]\n\n");
        
        template.append("PLAN:\n");
        template.append("• Treatment: [Medications, dressings]\n");
        template.append("• Follow-up: [Timeline]\n\n");
        
        template.append("💡 Image saved for reference. Complete template with your observations.\n");
        
        return template.toString();
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
