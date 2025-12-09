package za.co.ccos.infra.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object from LLAMA API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlamaResponse {
    private String content;
    private int processingTime;
    
    // Token usage metrics
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    
    // Model info
    private String model;
    private String finishReason;
    
    // Constructor for backward compatibility
    public LlamaResponse(String content) {
        this.content = content;
        this.processingTime = 0;
    }
    
    public LlamaResponse(String content, int processingTime) {
        this.content = content;
        this.processingTime = processingTime;
    }
}
