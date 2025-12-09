package za.co.ccos.web.dto;

import lombok.Data;
import za.co.ccos.domain.ConsultationState;
import za.co.ccos.domain.GeneratedNote;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConsultationDetailResponse {
    private UUID id;
    private UUID patientId;
    private UUID clinicianId;
    private LocalDateTime timestamp;
    private String rawTranscript;
    private ConsultationState state;
    private GeneratedNote generatedNote;
}
