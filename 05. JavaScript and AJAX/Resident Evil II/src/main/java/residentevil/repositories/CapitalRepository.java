package residentevil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import residentevil.domain.entities.Capital;

import java.util.List;

@Repository
public interface CapitalRepository extends JpaRepository<Capital, Long> {

    List<Capital> findAllByOrderByName();
}
