package za.co.ccos.infra.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "llama.provider", havingValue = "api")
@Slf4j
public class GroqLlamaAdapter implements LlamaAdapter {
    
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public GroqLlamaAdapter(@Value("${llama.groq.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public LlamaResponse runPrompt(String prompt, LlamaOptions options) throws LlamaException {
        try {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", new Object[]{
                    Map.of("role", "user", "content", prompt)
                },
                "temperature", options.getTemperature(),
                "max_tokens", options.getMaxTokens()
            ));
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            
            log.info("Sending request to Groq API");
            long startTime = System.currentTimeMillis();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long processingTime = System.currentTimeMillis() - startTime;
            
            if (response.statusCode() != 200) {
                log.error("Groq API error: {}", response.body());
                throw new LlamaException("Groq API returned status: " + response.statusCode());
            }
            
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode choice = root.path("choices").get(0);
            JsonNode usage = root.path("usage");
            
            String content = choice.path("message").path("content").asText();
            String finishReason = choice.path("finish_reason").asText();
            String model = root.path("model").asText();
            
            Integer promptTokens = usage.path("prompt_tokens").asInt();
            Integer completionTokens = usage.path("completion_tokens").asInt();
            Integer totalTokens = usage.path("total_tokens").asInt();
            
            log.info("Groq response: {}ms, {} tokens (prompt: {}, completion: {})", 
                    processingTime, totalTokens, promptTokens, completionTokens);
            
            return LlamaResponse.builder()
                    .content(content)
                    .processingTime((int) processingTime)
                    .promptTokens(promptTokens)
                    .completionTokens(completionTokens)
                    .totalTokens(totalTokens)
                    .model(model)
                    .finishReason(finishReason)
                    .build();
            
        } catch (Exception e) {
            log.error("Failed to communicate with Groq API", e);
            throw new LlamaException("Groq API error: " + e.getMessage(), e);
        }
    }
}
