package productshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import productshop.domain.entities.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.product ORDER BY o.orderDate DESC")
    List<Order> findAllEager();

    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.product WHERE o.id = :id")
    Optional<Order> findByIdEager(@Param("id") UUID id);

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.customer " +
            "JOIN FETCH o.product " +
            "WHERE o.customer.username = :username " +
            "AND o.isFinalized = true " +
            "ORDER BY o.orderDate DESC")
    List<Order> findAllFinalizedByUsernameEager(@Param("username") String username);

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.product " +
            "WHERE o.customer.username = :username " +
            "AND o.isFinalized = false")
    List<Order> findAllNotFinalizedByUsernameEager(@Param("username") String username);
}
