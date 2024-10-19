package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(OrderDto orderDto) {

    }

    public Order getById(String id) {
        return orderRepository.findById(id).orElse(null);
    }
}
