package productshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import productshop.domain.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
