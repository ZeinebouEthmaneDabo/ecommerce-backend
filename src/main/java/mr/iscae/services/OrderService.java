package mr.iscae.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mr.iscae.constants.OrderStatus;
import mr.iscae.dtos.requests.OrderItemRequest;
import mr.iscae.dtos.requests.OrderRequest;
import mr.iscae.dtos.requests.UpdateOrderRequest;
import mr.iscae.dtos.responses.OrderResponse;
import mr.iscae.dtos.responses.OrderItemResponse;
import mr.iscae.dtos.responses.UserOrderInfo;
import mr.iscae.dtos.responses.ProductOrderInfo;
import mr.iscae.entities.Order;
import mr.iscae.entities.OrderItem;
import mr.iscae.entities.Produit;
import mr.iscae.entities.User;
import mr.iscae.repositories.OrderRepository;
import mr.iscae.repositories.ProduitRepository;
import mr.iscae.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProduitRepository produitRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        // Find user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate total amount and create order items
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        // Create the order
        Order order = Order.builder()
                .user(user)
                .shippingAddress(request.getShippingAddress())
                .totalAmount(0.0) // Will be updated after calculating
                .build();

        // Process each order item
        for (OrderItemRequest itemRequest : request.getOrderItems()) {
            Produit product = produitRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id: " + itemRequest.getProductId()
                    ));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product: " + product.getName() +
                                ". Available: " + product.getStockQuantity() +
                                ", Requested: " + itemRequest.getQuantity()
                );
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .produit(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtOrder(product.getPrice())
                    .build();

            orderItems.add(orderItem);
            totalAmount += product.getPrice() * itemRequest.getQuantity();

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            produitRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());

        // Map user info
        UserOrderInfo userInfo = new UserOrderInfo();
        userInfo.setId(order.getUser().getId());
        userInfo.setFullName(order.getUser().getFullName());
        userInfo.setPhone(order.getUser().getPhone());
        response.setUser(userInfo);

        // Map order items
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());
        response.setOrderItems(itemResponses);

        return response;
    }
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        return mapToOrderResponse(order);
    }
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(OrderStatus status, Long userId) {
        List<Order> orders;

        if (status != null && userId != null) {
            orders = orderRepository.findByStatusAndUserId(status, userId);
        } else if (status != null) {
            orders = orderRepository.findByStatus(status);
        } else if (userId != null) {
            orders = orderRepository.findByUserId(userId);
        } else {
            orders = orderRepository.findAll();
        }

        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Only status updates are allowed
        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Order status must be provided");
        }

        // Check if order can be updated
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update delivered order");
        }

        // Validate status transition
        if (request.getStatus() == OrderStatus.CANCELLED) {
            if (order.getStatus() == OrderStatus.SHIPPED) {
                throw new IllegalStateException("Cannot cancel order that is already shipped or delivered");
            }
            returnItemsToStock(order);
        }

        // Update status
        order.setStatus(request.getStatus());

        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }
    private void returnItemsToStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Produit product = item.getProduit();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            produitRepository.save(product);
        }
    }
    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setPriceAtOrder(item.getPriceAtOrder());

        // Map product info
        ProductOrderInfo productInfo = new ProductOrderInfo();
        productInfo.setId(item.getProduit().getId());
        productInfo.setName(item.getProduit().getName());
        response.setProduct(productInfo);

        return response;
    }
}