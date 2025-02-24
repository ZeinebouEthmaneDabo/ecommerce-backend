package mr.iscae.dtos.requests;

import lombok.Data;
import mr.iscae.constants.OrderStatus;


@Data
public class UpdateOrderRequest {
    private OrderStatus status;
}