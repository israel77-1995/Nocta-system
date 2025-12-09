package za.co.ccos.web.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ApprovalRequest {
    private UUID clinicianId;
    private boolean approve;
}
