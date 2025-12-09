package za.co.ccos.web.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UploadRequest {
    private UUID patientId;
    private UUID clinicianId;
    private String rawTranscript;
    private String audioUrl;
}
