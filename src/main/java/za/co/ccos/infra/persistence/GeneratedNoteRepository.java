package za.co.ccos.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.ccos.domain.GeneratedNote;
import java.util.UUID;

@Repository
public interface GeneratedNoteRepository extends JpaRepository<GeneratedNote, UUID> {
}
