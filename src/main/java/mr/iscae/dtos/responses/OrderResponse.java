package mr.iscae.dtos.responses;

import lombok.Data;
import mr.iscae.constants.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private UserOrderInfo user;
    private Double totalAmount;
    private String shippingAddress;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> orderItems;
}
