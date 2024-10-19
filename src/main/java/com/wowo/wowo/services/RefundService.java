package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefundService {

    private final OrderRepository orderRepository;

    public RefundService(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

    public ResponseEntity<?> refund(String orderId) {
        final Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) throw new NotFoundException("Order not found");

        final Order order = orderOptional.get();
        if (order.getStatus().equals("SUCCESS")) {

        }
        else {
            throw new RuntimeException("Transaction status not found");
        }

        return ResponseEntity.ok().build();
    }
}
