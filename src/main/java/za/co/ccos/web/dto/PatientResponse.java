package za.co.ccos.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String medicalRecordNumber;
    private List<String> allergies;
    private List<String> chronicConditions;
}
