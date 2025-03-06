package mr.iscae.repositories;

import mr.iscae.constants.OrderStatus;
import mr.iscae.entities.Order;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByUserId(Long userId, Pageable pageable);
    Page<Order> findByStatusAndUserId(OrderStatus status, Long userId, Pageable pageable);
    @NotNull Page<Order> findAll(@NotNull Pageable pageable);
}
