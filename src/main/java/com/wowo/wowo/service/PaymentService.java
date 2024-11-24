package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.PaymentDto;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.model.WalletTransaction;
import com.wowo.wowo.repository.OrderRepository;
import com.wowo.wowo.util.AuthUtil;
import com.wowo.wowo.util.RequestUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
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
    private Order pay(String sourceId, Long orderId) {
        var order = orderService.getById(orderId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy đơn hàng"));
        return pay(sourceId, order);
    }

    /**
     * Thực hiện thanh toán cho đơn hàng,
     * sử dụng ví nguồn thanh toán là nguồn tiền của người dùng hiện tại
     *
     * @param paymentDto Thông tin thanh toán {@link PaymentDto}
     *
     * @return Đơn hàng đã được thanh toán
     */
    public Order pay(Long id, Authentication authentication) {
        Order order = isOrderValid(id);

        var partner = order.getPartner();
        var userId = ((String) authentication.getPrincipal());
        final Wallet partnerWallet = walletService.getPartnerWallet(partner.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ví đối tác"));
        final Wallet userWallet = walletService.getUserWallet(userId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví nguồn thanh toán"));

        final WalletTransaction walletTransaction = transferService.transferMoney(userWallet,
                partnerWallet, order.getDiscountMoney());

        walletTransaction.getTransaction()
                .setReceiverName(partner.getName());

        order.setStatus(PaymentStatus.SUCCESS);
        RequestUtil.sendRequest(order.getSuccessUrl(), "POST");
        return orderRepository.save(order);
    }

    private Order isOrderValid(Long orderId) {
        var order = orderService.getById(orderId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy đơn hàng"));
        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Đơn hàng đã được thanh toán");
        }
        return order;
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            backoff = @Backoff(delay = 1000)
    )
    public Order pay(@NotNull String sourceId, Order order) {
        var authId = AuthUtil.getId();
        var wallet = walletService.getWallet(Integer.parseInt(sourceId));
        if (!wallet.getOwnerId()
                .equals(authId)) {
            throw new NotFoundException("Không tìm thấy ví");
        }

        final String partnerId = order.getPartner()
                .getId();
        var partnerWallet = walletService.getPartnerWallet(partnerId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví đối tác"));

        final WalletTransaction walletTransaction = transferService.transferMoney(wallet,
                partnerWallet, order.getMoney());
        order.setTransaction(walletTransaction.getTransaction());
        return order;
    }
}
