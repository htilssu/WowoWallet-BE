package com.wowo.wowo.service;

import com.wowo.wowo.CheckOrderTask;
import com.wowo.wowo.data.dto.OrderCreateDTO;
import com.wowo.wowo.data.dto.OrderDTO;
import com.wowo.wowo.data.dto.OrderItemCreateDTO;
import com.wowo.wowo.data.mapper.OrderItemMapper;
import com.wowo.wowo.data.mapper.OrderMapper;
import com.wowo.wowo.data.mapper.OrderMapperImpl;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.kafka.message.UseVoucherMessage;
import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.OrderItem;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.Voucher;
import com.wowo.wowo.repository.OrderItemRepository;
import com.wowo.wowo.repository.OrderRepository;
import com.wowo.wowo.repository.VoucherRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapperImpl orderMapperImpl;
    private final PartnerService partnerService;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final RefundService refundService;
    private final OrderMapper orderMapper;
    private final VoucherRepository voucherRepository;
    private final VoucherService voucherService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private final ConstantService constantService;

    public OrderDTO createOrder(Order order,
            Collection<OrderItemCreateDTO> orderItemCreateDTOs,
            Authentication authentication) {
        var partnerId = authentication.getPrincipal()
                .toString();
        var partner = partnerService.getPartnerById(partnerId)
                .orElseThrow(() -> new BadRequest("Không tìm thấy đối tác"));

        order.setPartner(partner);
        final Order newOrder = orderRepository.save(order);
        var orderItems = orderItemCreateDTOs.stream()
                .map(orderItemMapper::toEntity)
                .toList();
        orderItems = orderItems.stream()
                .peek(orderItem -> orderItem.setOrderId(newOrder.getId()))
                .toList();

        orderItemRepository.saveAll(orderItems);

        final OrderDTO orderDTO = orderMapper.toDto(newOrder);
        orderDTO.setItems(orderItemCreateDTOs);
        orderDTO.setVouchers(Collections.emptyList());
        orderDTO.setCheckoutUrl("https://wowo.htilssu.id.vn/order/" + order.getId());

        return orderDTO;
    }

    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO, Authentication authentication) {
        Order order = orderMapperImpl.toEntity(orderCreateDTO);
        order.setDiscountMoney(order.getMoney());
        createCheckOrderJob(order);
        return createOrder(order, orderCreateDTO.items(), authentication);
    }

    private void createCheckOrderJob(Order order) {
        scheduler.schedule(new CheckOrderTask(order, this), constantService.getOrderMaxLifeTime()
                .longValue(), TimeUnit.MINUTES);
    }

    public Optional<Order> getById(Long id) {
        return orderRepository.findById(id);
    }

    public OrderDTO getOrderDetail(Long id) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));
        final Collection<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        final Collection<Voucher> voucher = voucherRepository.findByOrderId(order.getId());
        final OrderDTO orderDTO = orderMapperImpl.toDto(order);
        orderDTO.setItems(orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList());
        orderDTO.setVouchers(voucher);
        orderDTO.setCheckoutUrl("https://wowo.htilssu.id.vn/orders/" + order.getId());
        return orderDTO;
    }

    public Order cancelOrder(Long id, Authentication authentication) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));

        if (!order.getPartner()
                .getId()
                .equals(authentication.getPrincipal()
                        .toString())) {
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
        final Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));

        if (!order.getPartner()
                .getId()
                .equals(authentication.getPrincipal()
                        .toString())) {
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

    public Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));
    }

    public Order useVoucher(UseVoucherMessage message) {
        final Order order = getOrderOrThrow(message.OrderID());
        Voucher voucher = new Voucher(null, message.VoucherID(), message.VoucherName(),
                message.Discount(), order.getId());
        order.useVoucher(voucher);
        voucherService.save(voucher);
        return orderRepository.save(order);
    }

    public void cancelOrder(Order orderInDb) {
        orderInDb.cancel();
        orderRepository.save(orderInDb);
    }
}