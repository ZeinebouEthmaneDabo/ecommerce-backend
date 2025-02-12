package mr.iscae.dtos.responses;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private ProductOrderInfo product;
    private Integer quantity;
    private Double priceAtOrder;
}
