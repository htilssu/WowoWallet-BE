package com.wowo.wowo.presentation.order;

import com.wowo.wowo.application.order.OrderApplicationService;
import com.wowo.wowo.domain.order.entity.OrderAggregate;
import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.order.valueobjects.OrderStatus;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import com.wowo.wowo.presentation.order.OrderRequestDTOs.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DDD Order Controller - Clean API endpoints for order management
 * Follows DDD principles with thin controller delegating to application services
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders (DDD)", description = "Order management with Domain-Driven Design")
public class OrderDddController {
    
    private final OrderApplicationService orderApplicationService;
    
    @PostMapping
    @Operation(summary = "Create new order")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderId orderId = orderApplicationService.createOrder(
            request.getApplicationId(),
            request.getAmount(),
            request.getCurrency(),
            request.getReturnUrl(),
            request.getSuccessUrl(),
            request.getServiceName()
        );
        
        OrderAggregate order = orderApplicationService.getOrderById(orderId)
            .orElseThrow(() -> new RuntimeException("Failed to retrieve created order"));
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(toResponse(order));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return orderApplicationService.getOrderById(OrderId.of(id))
            .map(order -> ResponseEntity.ok(toResponse(order)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Get all orders with pagination")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<OrderAggregate> orders = orderApplicationService.getAllOrders(page, size);
        List<OrderResponse> responses = orders.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/application/{applicationId}")
    @Operation(summary = "Get orders by application")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<List<OrderResponse>> getOrdersByApplication(@PathVariable String applicationId) {
        List<OrderAggregate> orders = orderApplicationService.getOrdersByApplication(applicationId);
        List<OrderResponse> responses = orders.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<OrderAggregate> orders = orderApplicationService.getOrdersByStatus(orderStatus);
            List<OrderResponse> responses = orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/discount")
    @Operation(summary = "Apply discount to order")
    @ApiResponse(responseCode = "200", description = "Discount applied successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "400", description = "Invalid discount or order state")
    public ResponseEntity<OrderResponse> applyDiscount(
            @PathVariable Long id,
            @Valid @RequestBody ApplyDiscountRequest request) {
        
        try {
            orderApplicationService.applyDiscount(
                OrderId.of(id),
                request.getDiscountAmount(),
                request.getCurrency()
            );
            
            OrderAggregate order = orderApplicationService.getOrderById(OrderId.of(id))
                .orElseThrow(() -> new RuntimeException("Order not found after discount"));
            
            return ResponseEntity.ok(toResponse(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete order with payment")
    @ApiResponse(responseCode = "200", description = "Order completed successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "400", description = "Invalid payment or order state")
    public ResponseEntity<OrderResponse> completeOrder(
            @PathVariable Long id,
            @Valid @RequestBody CompleteOrderRequest request) {
        
        try {
            orderApplicationService.completeOrder(
                OrderId.of(id),
                TransactionId.of(request.getTransactionId()),
                request.getPaidAmount(),
                request.getCurrency()
            );
            
            OrderAggregate order = orderApplicationService.getOrderById(OrderId.of(id))
                .orElseThrow(() -> new RuntimeException("Order not found after completion"));
            
            return ResponseEntity.ok(toResponse(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    @ApiResponse(responseCode = "200", description = "Order cancelled successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "400", description = "Order cannot be cancelled")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        try {
            orderApplicationService.cancelOrder(OrderId.of(id));
            
            OrderAggregate order = orderApplicationService.getOrderById(OrderId.of(id))
                .orElseThrow(() -> new RuntimeException("Order not found after cancellation"));
            
            return ResponseEntity.ok(toResponse(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund order")
    @ApiResponse(responseCode = "200", description = "Order refunded successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "400", description = "Invalid refund or order state")
    public ResponseEntity<OrderResponse> refundOrder(
            @PathVariable Long id,
            @Valid @RequestBody RefundOrderRequest request) {
        
        try {
            orderApplicationService.refundOrder(
                OrderId.of(id),
                request.getRefundAmount(),
                request.getCurrency()
            );
            
            OrderAggregate order = orderApplicationService.getOrderById(OrderId.of(id))
                .orElseThrow(() -> new RuntimeException("Order not found after refund"));
            
            return ResponseEntity.ok(toResponse(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/can-cancel")
    @Operation(summary = "Check if order can be cancelled")
    @ApiResponse(responseCode = "200", description = "Cancellation status checked")
    public ResponseEntity<Boolean> canCancelOrder(@PathVariable Long id) {
        boolean canCancel = orderApplicationService.canCancelOrder(OrderId.of(id));
        return ResponseEntity.ok(canCancel);
    }
    
    @GetMapping("/application/{applicationId}/revenue")
    @Operation(summary = "Get application revenue")
    @ApiResponse(responseCode = "200", description = "Revenue calculated successfully")
    public ResponseEntity<String> getApplicationRevenue(@PathVariable String applicationId) {
        var revenue = orderApplicationService.getApplicationRevenue(applicationId);
        return ResponseEntity.ok(revenue.toString());
    }
    
    /**
     * Convert domain model to response DTO
     */
    private OrderResponse toResponse(OrderAggregate order) {
        return OrderResponse.builder()
            .id(order.getId().getValue())
            .applicationId(order.getApplicationId().getValue().toString())
            .amount(BigDecimal.valueOf(order.getAmount().getAmount()))
            .discountAmount(BigDecimal.valueOf(order.getDiscountAmount().getAmount()))
            .finalAmount(BigDecimal.valueOf(order.getFinalAmount().getAmount()))
            .currency(order.getAmount().getCurrency())
            .status(order.getStatus().name())
            .transactionId(order.getTransactionId() != null ? order.getTransactionId().getValue() : null)
            .returnUrl(order.getUrls().getReturnUrl())
            .successUrl(order.getUrls().getSuccessUrl())
            .serviceName(order.getServiceName())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .canBeProcessed(order.canBeProcessed())
            .canBeCancelled(order.isPending())
            .build();
    }
}