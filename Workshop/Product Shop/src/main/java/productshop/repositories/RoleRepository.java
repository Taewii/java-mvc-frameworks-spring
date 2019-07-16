package productshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productshop.domain.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
