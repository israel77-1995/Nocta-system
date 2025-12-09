package za.co.ccos.infra.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class GroqVisionAdapter {
    
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public GroqVisionAdapter(@Value("${llama.groq.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public String analyzeImage(String base64Image, String prompt) throws LlamaException {
        try {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                "model", "llama-3.2-11b-vision-preview",
                "messages", new Object[]{
                    Map.of(
                        "role", "user",
                        "content", new Object[]{
                            Map.of("type", "text", "text", prompt),
                            Map.of(
                                "type", "image_url",
                                "image_url", Map.of("url", "data:image/jpeg;base64," + base64Image)
                            )
                        }
                    )
                },
                "temperature", 0.2,
                "max_tokens", 500
            ));
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            
            log.info("Sending image to Groq Vision API");
            long startTime = System.currentTimeMillis();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long processingTime = System.currentTimeMillis() - startTime;
            
            if (response.statusCode() != 200) {
                log.error("Groq Vision API error: {}", response.body());
                throw new LlamaException("Groq Vision API returned status: " + response.statusCode());
            }
            
            JsonNode root = objectMapper.readTree(response.body());
            String content = root.path("choices").get(0).path("message").path("content").asText();
            
            log.info("Groq Vision response received in {}ms", processingTime);
            return content;
            
        } catch (Exception e) {
            log.error("Failed to communicate with Groq Vision API", e);
            throw new LlamaException("Groq Vision API error: " + e.getMessage(), e);
        }
    }
}
