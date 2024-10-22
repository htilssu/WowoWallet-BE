package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.OrderCreateDto;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.data.dto.OrderItemCreateDto;
import com.wowo.wowo.data.mapper.OrderItemMapper;
import com.wowo.wowo.data.mapper.OrderMapperImpl;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.mongo.documents.OrderItem;
import com.wowo.wowo.mongo.repositories.OrderItemRepository;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapperImpl orderMapperImpl;
    private final PartnerService partnerService;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public void createOrder(Order order, Collection<OrderItemCreateDto> orderItemCreateDtos) {
        var partnerId = AuthUtil.getId();
        var partner = partnerService.getPartnerById(partnerId);
        order.setPartner(partner);
        final Order newOrder = orderRepository.save(order);
        var orderItems = orderItemCreateDtos.stream().map(orderItemMapper::toEntity).toList();
        orderItems = orderItems.stream().peek(orderItem -> orderItem.setOrderId(newOrder.getId()))
                .toList();

        orderItemRepository.saveAll(orderItems);
    }

    public OrderDto createOrder(OrderCreateDto orderCreateDto) {
        Order order = orderMapperImpl.toEntity(orderCreateDto);
        createOrder(order, orderCreateDto.items());
        final OrderDto orderDto = orderMapperImpl.toDto(order);

        orderDto.setItems(orderCreateDto.items());
        return orderDto;
    }

    public Optional<Order> getById(String id) {
        return orderRepository.findById(id);
    }

    public OrderDto getOrderDetail(String id) {
        final Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy đơn hàng"));
        final Collection<OrderItem> orderItems = orderItemRepository.findByOrderId(
                Long.valueOf(id));
        final OrderDto orderDto = orderMapperImpl.toDto(order);
        orderDto.setItems(orderItems.stream().map(orderItemMapper::toDto).toList());
        return orderDto;
    }
}