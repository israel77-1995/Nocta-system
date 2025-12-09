package za.co.ccos.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import za.co.ccos.domain.ConsultationState;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StatusResponse {
    private UUID consultationId;
    private ConsultationState state;
    private String errorMessage;
}
