package za.co.ccos.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalysisResponse {
    private boolean success;
    private String analysis;
    private String error;
}
