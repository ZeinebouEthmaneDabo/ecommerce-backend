package mr.iscae.repositories;

import mr.iscae.constants.OrderStatus;
import mr.iscae.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserId(Long userId);
    List<Order> findByStatusAndUserId(OrderStatus status, Long userId);
}