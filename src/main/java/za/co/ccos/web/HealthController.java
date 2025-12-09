package za.co.ccos.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.ccos.infra.llm.LlamaAdapter;
import za.co.ccos.infra.llm.LlamaOptions;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HealthController {
    
    private final LlamaAdapter llamaAdapter;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "clinical-copilot");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/llama/health")
    public ResponseEntity<Map<String, String>> llamaHealth() {
        Map<String, String> response = new HashMap<>();
        try {
            llamaAdapter.runPrompt("test", LlamaOptions.builder().maxTokens(5).build());
            response.put("status", "UP");
            response.put("message", "Llama server is reachable");
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("message", e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
