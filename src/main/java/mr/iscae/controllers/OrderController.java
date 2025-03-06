package mr.iscae.controllers;


import mr.iscae.constants.OrderStatus;
import mr.iscae.dtos.requests.OrderRequest;
import mr.iscae.dtos.requests.UpdateOrderRequest;
import mr.iscae.dtos.responses.OrderResponse;
import mr.iscae.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private  OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestHeader("Authorization") String authorizationToken,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Extract the actual token (remove "Bearer " prefix)
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;

        Page<OrderResponse> orders = orderService.getAllOrders(token, status, userId, page, size);
        return ResponseEntity.ok(orders);
    }




    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }
}