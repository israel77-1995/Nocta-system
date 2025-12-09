package za.co.ccos.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientSummaryResponse {
    private UUID patientId;
    private String summary;
}
