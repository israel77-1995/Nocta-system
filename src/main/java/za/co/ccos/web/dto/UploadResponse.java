package za.co.ccos.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import za.co.ccos.domain.ConsultationState;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UploadResponse {
    private UUID consultationId;
    private ConsultationState status;
}
