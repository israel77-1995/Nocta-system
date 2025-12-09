package za.co.ccos.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VitalSigns {
    private String bloodPressure;
    private Integer heartRate;
    private Double temperature;
    private Integer oxygenSaturation;
    private Integer respiratoryRate;
    private Double weight;
    private Double height;
}
