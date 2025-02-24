package mr.iscae.dtos.responses;

import lombok.Data;
import mr.iscae.constants.OrderStatus;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private UserOrderInfo user;
    private Double totalAmount;
    private String shippingAddress;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;
}
