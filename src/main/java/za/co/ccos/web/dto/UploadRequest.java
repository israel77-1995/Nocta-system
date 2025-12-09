package za.co.ccos.web.dto;

import lombok.Data;
import za.co.ccos.domain.VitalSigns;
import java.util.UUID;

@Data
public class UploadRequest {
    private UUID patientId;
    private UUID clinicianId;
    private String rawTranscript;
    private String audioUrl;
    private VitalSigns vitalSigns;
}
