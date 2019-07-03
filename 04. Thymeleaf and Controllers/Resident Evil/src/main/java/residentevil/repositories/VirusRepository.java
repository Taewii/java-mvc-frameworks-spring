package residentevil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import residentevil.domain.entities.Virus;

import java.util.UUID;

@Repository
public interface VirusRepository extends JpaRepository<Virus, UUID> {
}
