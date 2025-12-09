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

@Component
@ConditionalOnProperty(name = "llama.provider", havingValue = "never")
@Slf4j
public class HttpLlamaAdapter implements LlamaAdapter {
    
    private final String llamaServerUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public HttpLlamaAdapter(@Value("${llama.server.url}") String llamaServerUrl) {
        this.llamaServerUrl = llamaServerUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public LlamaResponse runPrompt(String prompt, LlamaOptions options) throws LlamaException {
        try {
            String requestBody = buildRequestBody(prompt, options);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(llamaServerUrl + "/completion"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(120))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            
            log.info("Sending request to LLAMA server: {}", llamaServerUrl);
            long startTime = System.currentTimeMillis();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            long processingTime = (int) (System.currentTimeMillis() - startTime);
            
            if (response.statusCode() != 200) {
                throw new LlamaException("LLAMA server returned status: " + response.statusCode());
            }
            
            String content = parseResponse(response.body());
            log.info("LLAMA response received in {}ms", processingTime);
            
            return new LlamaResponse(content, (int) processingTime);
            
        } catch (Exception e) {
            log.error("Failed to communicate with LLAMA server", e);
            throw new LlamaException("LLAMA server error: " + e.getMessage(), e);
        }
    }
    
    private String buildRequestBody(String prompt, LlamaOptions options) throws Exception {
        return objectMapper.writeValueAsString(new RequestPayload(
                prompt,
                options.getTemperature(),
                options.getMaxTokens(),
                0.9,
                40,
                false
        ));
    }
    
    private String parseResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        return root.path("content").asText();
    }
    
    private record RequestPayload(
            String prompt,
            double temperature,
            int n_predict,
            double top_p,
            int top_k,
            boolean stream
    ) {}
}
