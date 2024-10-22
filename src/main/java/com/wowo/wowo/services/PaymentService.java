package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.PaymentDto;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.models.PaymentStatus;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.util.AuthUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentService {

    private final OrderService orderService;
    private final TransferService transferService;
    private final WalletService walletService;
    private final OrderRepository orderRepository;

    /**
     * Thực hiện thanh toán cho đơn hàng,
     * sử dụng ví nguồn thanh toán là nguồn tiền của người dùng hiện tại
     *
     * @param sourceId Id của nguồn thanh toán {@link  Wallet}
     * @param orderId  Id của order muốn thanh toán {@link Order}
     *
     * @return Đơn hàng đã được thanh toán
     */
    private Order pay(String sourceId, String orderId) {
        var order = orderService.getById(orderId).orElseThrow(
                () -> new NotFoundException("Không tìm thấy đơn hàng"));
        return pay(sourceId, order);
    }

    public Order pay(@Valid PaymentDto paymentDto) {
        Order order;

        switch (paymentDto.getPaymentService()) {
            case WALLET -> order = pay(paymentDto.getSourceId(), paymentDto.getOrderId());
            case PAYPAL -> order = pay(paymentDto.getSourceId(), paymentDto.getOrderId());
            default -> {
                throw new RuntimeException("Dịch vụ thanh toán không hợp lệ");
            }
        }

        order.setStatus(PaymentStatus.SUCCESS);
        return orderRepository.save(order);
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            backoff = @Backoff(delay = 1000)
    )
    public Order pay(@NotNull String sourceId, Order order) {
        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Đơn hàng đã được thanh toán");
        }

        var authId = AuthUtil.getId();
        var wallet = walletService.getWallet(Integer.parseInt(sourceId));
        if (!wallet.getOwnerId().equals(authId)) {
            throw new NotFoundException("Không tìm thấy ví");
        }

        final String partnerId = order.getPartner().getId();
        var partnerWallet = walletService.getPartnerWallet(partnerId).orElseThrow(
                () -> new NotFoundException("Không tìm thấy ví đối tác"));

        transferService.transferMoney(wallet, partnerWallet, order.getMoney());

        return order;
    }
}
