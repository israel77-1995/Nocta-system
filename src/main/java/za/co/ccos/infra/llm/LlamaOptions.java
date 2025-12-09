package za.co.ccos.infra.llm;

import lombok.Builder;
import lombok.Data;

/**
 * Configuration options for LLAMA API calls
 */
@Data
@Builder
public class LlamaOptions {
    private double temperature;
    private int maxTokens;
    private String systemPrompt;
}
