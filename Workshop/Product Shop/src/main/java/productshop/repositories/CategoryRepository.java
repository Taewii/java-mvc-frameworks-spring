package productshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import productshop.domain.entities.Category;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(@NotBlank String name);

    @Query("SELECT c FROM Category c JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdEager(@Param("id") Long id);
}
