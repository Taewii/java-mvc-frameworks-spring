package residentevil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import residentevil.domain.entities.User;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsername(@NotBlank String username);

    @Query(value = "SELECT COUNT(u) FROM User u WHERE u.authority = residentevil.domain.enums.Authority.ROOT")
    int rootCount();
}
