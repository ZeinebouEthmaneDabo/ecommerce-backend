package mr.iscae.dtos.requests;

import jakarta.validation.Valid;
import lombok.Data;
import mr.iscae.constants.OrderStatus;
import java.util.List;

@Data
public class UpdateOrderRequest {
    private String shippingAddress;
    private OrderStatus status;
    @Valid
    private List<OrderItemRequest> orderItems;
}