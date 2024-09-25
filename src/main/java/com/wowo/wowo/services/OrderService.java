package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.response.OrderDto;
import com.wowo.wowo.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(OrderDto orderDto){

    }
}
