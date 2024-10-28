package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.OrderCreateDto;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.data.dto.OrderItemCreateDto;
import com.wowo.wowo.data.mapper.OrderItemMapper;
import com.wowo.wowo.data.mapper.OrderMapperImpl;
import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.models.PaymentStatus;
import com.wowo.wowo.mongo.documents.OrderItem;
import com.wowo.wowo.mongo.repositories.OrderItemRepository;
import com.wowo.wowo.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapperImpl orderMapperImpl;

    @Mock
    private PartnerService partnerService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private RefundService refundService;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private Authentication authentication;

    OrderCreateDto orderCreateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderCreateDto = new OrderCreateDto(1000L,
                List.of(new OrderItemCreateDto("1", 1L, 1000L)), "returnUrl", "successUrl",
                "serviceName");
        when(authentication.getPrincipal()).thenReturn("partnerId");
        when(partnerService.getPartnerById("partnerId")).thenReturn(Optional.of(new Partner()));
    }

    @Test
    void createOrder_success() {
        Order order = new Order();

        when(orderRepository.save(order)).thenReturn(order);
        when(orderItemMapper.toEntity(any())).thenReturn(new OrderItem());
        when(orderMapperImpl.toEntity(any(OrderCreateDto.class))).thenReturn(order);
        when(orderMapperImpl.toDto(any())).thenReturn(new OrderDto());


        orderService.createOrder(orderCreateDto, authentication);

        verify(orderRepository, times(1)).save(order);
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getOrderDetail_success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(
                List.of(orderItem));
        when(orderItemMapper.toDto(orderItem)).thenReturn(new OrderItemCreateDto("1", 1L, 1000L));
        when(orderMapperImpl.toDto(order)).thenReturn(new OrderDto().setId(orderId));

        OrderDto orderDto = orderService.getOrderDetail(orderId);

        assertNotNull(orderDto);
        assertEquals(orderId, orderDto.getId());
        assertFalse(orderDto.getItems().isEmpty());
    }

    @Test
    void getOrderDetail_notFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.getOrderDetail(orderId));
    }

    @Test
    void cancelOrder_success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(PaymentStatus.PENDING);
        Partner partner = new Partner();
        partner.setId("partnerId");
        order.setPartner(partner);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(authentication.getPrincipal()).thenReturn("partnerId");
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.cancelOrder(orderId, authentication);

        assertEquals(PaymentStatus.CANCELLED, result.getStatus());
    }

    @Test
    void cancelOrder_notFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> orderService.cancelOrder(orderId, authentication));
    }

    @Test
    void cancelOrder_unauthorized() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        var partner = new Partner();
        partner.setId("partnerId");
        order.setPartner(partner);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(authentication.getPrincipal()).thenReturn("partnerIdD");

        assertThrows(BadRequest.class, () -> orderService.cancelOrder(orderId, authentication));
    }

    @Test
    void refundOrder_success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(PaymentStatus.SUCCESS);
        Partner partner = new Partner();
        partner.setId("partnerId");
        order.setPartner(partner);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(authentication.getPrincipal()).thenReturn("partnerId");
        when(refundService.refund(order)).thenReturn(new Order(){
            {setStatus(PaymentStatus.REFUNDED);}
        });

        Order result = orderService.refundOrder(orderId, authentication);

        assertEquals(PaymentStatus.REFUNDED, result.getStatus());
    }

    @Test
    void refundOrder_notFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> orderService.refundOrder(orderId, authentication));
    }

    @Test
    void refundOrder_unauthorized() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        Partner partner = new Partner();
        partner.setId("partnerId");
        order.setPartner(partner);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(authentication.getPrincipal()).thenReturn("partnerIdD");

        assertThrows(BadRequest.class, () -> orderService.refundOrder(orderId, authentication));
    }
}