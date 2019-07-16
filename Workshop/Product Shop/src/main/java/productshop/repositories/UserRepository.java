package productshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productshop.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsername(String username);

//    @Query("SELECT u.roles FROM User u WHERE u.id = :id")
//    List<Role> getUserRoles(@Param("id") UUID id);
}
