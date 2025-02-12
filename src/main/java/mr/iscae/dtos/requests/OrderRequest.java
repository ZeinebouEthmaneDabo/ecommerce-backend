package mr.iscae.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Shipping address is required")
    private String shippingAddress;

    @NotEmpty(message = "Order items are required")
    @Size(min = 1, message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> orderItems;
}

