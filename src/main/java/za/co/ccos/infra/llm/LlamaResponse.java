package za.co.ccos.infra.llm;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response object from LLAMA API
 */
@Data
@AllArgsConstructor
public class LlamaResponse {
    private String content;
    private int processingTime;
    
    // Constructor for backward compatibility
    public LlamaResponse(String content) {
        this.content = content;
        this.processingTime = 0;
    }
}
