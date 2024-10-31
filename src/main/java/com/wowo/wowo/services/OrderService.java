package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.OrderCreateDto;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.data.dto.OrderItemCreateDto;
import com.wowo.wowo.data.mapper.OrderItemMapper;
import com.wowo.wowo.data.mapper.OrderMapperImpl;
import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.models.PaymentStatus;
import com.wowo.wowo.mongo.documents.OrderItem;
import com.wowo.wowo.mongo.repositories.OrderItemRepository;
import com.wowo.wowo.repositories.OrderRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private final RefundService refundService;

    public void createOrder(Order order, Collection<OrderItemCreateDto> orderItemCreateDtos,
            Authentication authentication) {
        var partnerId = authentication.getPrincipal().toString();
        var partner = partnerService.getPartnerById(partnerId).orElseThrow(
                () -> new BadRequest("Không tìm thấy đối tác"));

        order.setPartner(partner);
        final Order newOrder = orderRepository.save(order);
        var orderItems = orderItemCreateDtos.stream().map(orderItemMapper::toEntity).toList();
        orderItems = orderItems.stream().peek(orderItem -> orderItem.setOrderId(newOrder.getId()))
                .toList();

        orderItemRepository.saveAll(orderItems);
    }

    public OrderDto createOrder(OrderCreateDto orderCreateDto, Authentication authentication) {
        Order order = orderMapperImpl.toEntity(orderCreateDto);
        createOrder(order, orderCreateDto.items(), authentication);
        final OrderDto orderDto = orderMapperImpl.toDto(order);

        orderDto.setItems(orderCreateDto.items());
        return orderDto;
    }

    public Optional<Order> getById(Long id) {
        return orderRepository.findById(id);
    }

    public OrderDto getOrderDetail(Long id) {
        final Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy đơn hàng"));
        final Collection<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        final OrderDto orderDto = orderMapperImpl.toDto(order);
        orderDto.setItems(orderItems.stream().map(orderItemMapper::toDto).toList());
        orderDto.setCheckoutUrl("https://wowo.htilssu.id.vn/orders/" + order.getId());
        return orderDto;
    }

    public Order cancelOrder(Long id, Authentication authentication) {
        final Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy đơn hàng"));

        if (!order.getPartner().getId().equals(authentication.getPrincipal().toString())) {
            throw new BadRequest("Không thể hủy đơn hàng của đối tác khác");
        }

        switch (order.getStatus()) {
            case PENDING -> {
                order.setStatus(PaymentStatus.CANCELLED);
            }
            case SUCCESS -> {
                throw new BadRequest("Không thể hủy đơn hàng đã thanh toán");
            }
            case REFUNDED -> {
                throw new BadRequest("Đơn hàng đã được hoàn tiền");
            }
            default -> throw new IllegalStateException("Unexpected value: " + order.getStatus());
        }


        return orderRepository.save(order);
    }

    public Order refundOrder(@NotNull Long id, Authentication authentication) {
        final Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy đơn hàng"));

        if (!order.getPartner().getId().equals(authentication.getPrincipal().toString())) {
            throw new BadRequest("Không thể hủy đơn hàng của đối tác khác");
        }

        switch (order.getStatus()) {
            case PENDING -> {
                throw new BadRequest("Không thể hoàn tiền đơn hàng chưa thanh toán");
            }
            case SUCCESS -> {
                return refundService.refund(order);
            }
            case REFUNDED -> {
                throw new BadRequest("Đơn hàng đã được hoàn tiền");
            }
            default -> throw new IllegalStateException("Unexpected value: " + order.getStatus());
        }
    }
}