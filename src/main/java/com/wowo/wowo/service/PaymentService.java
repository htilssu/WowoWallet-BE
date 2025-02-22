package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.PaymentDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.OrderRepository;
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
    private final UserService userService;

    /**
     * Thực hiện thanh toán cho đơn hàng,
     * sử dụng ví nguồn thanh toán là nguồn tiền của người dùng hiện tại
     *
     * @param sourceId Id của nguồn thanh toán {@link  UserWallet}
     * @param orderId  Id của order muốn thanh toán {@link Order}
     *
     * @return Đơn hàng đã được thanh toán
     */
    private Order pay(Long sourceId, Long orderId) {
        var order = orderService.getById(orderId)
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy đơn hàng"));
        var wallet = walletService.getWallet(sourceId);
        return pay(wallet, order);
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            backoff = @Backoff(delay = 1000)
    )
    public Order pay(@NotNull Wallet wallet, Order order) {
        var applicationWallet = order.getApplication()
                .getWallet();

        final Transaction transaction = transferService.transferMoney(wallet,
                applicationWallet, order.getDiscountMoney());
        order.setTransaction(transaction);
        return order;
    }

    /**
     * Thực hiện thanh toán cho đơn hàng,
     * sử dụng ví nguồn thanh toán là nguồn tiền của người dùng hiện tại
     *
     * @return Đơn hàng đã được thanh toán
     */
    public Order pay(Long id, Authentication authentication) {
        Order order = orderService.getOrderOrThrow(id);
        isOrderValid(order);

        var application = order.getApplication();
        var userId = ((String) authentication.getPrincipal());

        final User user = userService.getUserByIdOrElseThrow(userId);

        final ApplicationWallet applicationWallet = application.getWallet();
        final UserWallet userWallet = user.getWallet();

        final Transaction transaction = transferService.transferMoney(userWallet,
                applicationWallet, order.getDiscountMoney());

        transaction.setReceiverName(application.getName());
        transaction.setSenderName(user.getFullName());
        order.setTransaction(transaction);

        order.setStatus(PaymentStatus.SUCCESS);
        final Order orderSaved = orderRepository.save(order);
        RequestUtil.sendRequest(order.getSuccessUrl(), "POST");
        return orderSaved;
    }

    private void isOrderValid(Order order) {
        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new BadRequest("Đơn hàng không hợp lệ");
        }
    }
}
