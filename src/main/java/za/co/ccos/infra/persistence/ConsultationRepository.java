package za.co.ccos.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.ccos.domain.Consultation;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    List<Consultation> findByPatientIdOrderByCreatedAtDesc(UUID patientId);
}
