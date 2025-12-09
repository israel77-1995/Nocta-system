package za.co.ccos.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlamaMetricsResponse {
    private String consultationId;
    private PerceptionMetrics perception;
    private DocumentationMetrics documentation;
    private CoordinationMetrics coordination;
    private ComplianceMetrics compliance;
    private TotalMetrics total;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerceptionMetrics {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
        private Integer processingTimeMs;
        private String model;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentationMetrics {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
        private Integer processingTimeMs;
        private String model;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoordinationMetrics {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
        private Integer processingTimeMs;
        private String model;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComplianceMetrics {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
        private Integer processingTimeMs;
        private String model;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalMetrics {
        private Integer totalPromptTokens;
        private Integer totalCompletionTokens;
        private Integer totalTokens;
        private Integer totalProcessingTimeMs;
        private Double estimatedCostUsd;
    }
}
