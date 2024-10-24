package com.wowo.wowo.services;

import com.wowo.wowo.constants.Constant;
import com.wowo.wowo.data.dto.PaymentDto;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private OrderService orderService;

    @Mock
    private TransferService transferService;

    @Mock
    private WalletService walletService;

    @Mock
    private OrderRepository orderRepository;

    private Partner partner;

    @BeforeEach
    void setUp() {
        partner = getPartner();
    }

    @Test
    void pay_OrderNotFound_ThrowsNotFoundException() {
        when(orderService.getById(anyString())).thenReturn(Optional.empty());

        // Tạo PaymentDto
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setSourceId("1");
        paymentDto.setOrderId("123");
        paymentDto.setPaymentService(Constant.PaymentService.WALLET);

        assertThrows(NotFoundException.class, () -> paymentService.pay(paymentDto));

        verify(orderService).getById("123");
    }

    @Test
    void pay_OrderAlreadyPaid_ThrowsException() {
        Order order = new Order();
        order.setStatus(PaymentStatus.SUCCESS);

        when(orderService.getById(anyString())).thenReturn(Optional.of(order));

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setSourceId("1");
        paymentDto.setOrderId("123");
        paymentDto.setPaymentService(Constant.PaymentService.WALLET);

        assertThrows(RuntimeException.class, () -> paymentService.pay(paymentDto));
    }

    @Nested
    class PayByWallet {

        @Test
        void pay_ValidOrder_SuccessfullyPaid() {

            try (MockedStatic<AuthUtil> authUtilMockedStatic = Mockito.mockStatic(AuthUtil.class)) {
                authUtilMockedStatic.when(AuthUtil::getId).thenReturn("user123");
                Order order = new Order();
                order.setStatus(PaymentStatus.PENDING);
                order.setMoney(1000L);
                order.setPartner(partner);
                Wallet wallet = new Wallet();
                wallet.setBalance(99999L);
                wallet.setOwnerId("user123");
                Wallet partnerWallet = new Wallet();

                when(orderService.getById(anyString())).thenReturn(Optional.of(order));
                when(walletService.getWallet(anyInt())).thenReturn(wallet);
                when(walletService.getPartnerWallet(anyString())).thenReturn(
                        Optional.of(partnerWallet));
                when(orderRepository.save(any(Order.class))).thenReturn(order);
                when(transferService.transferMoney(any(Wallet.class), any(Wallet.class),
                        anyLong())).thenReturn(getWalletTransaction());

                PaymentDto paymentDto = new PaymentDto();
                paymentDto.setSourceId("1");
                paymentDto.setOrderId("123");
                paymentDto.setPaymentService(Constant.PaymentService.WALLET);

                Order result = paymentService.pay(paymentDto);

                assertEquals(PaymentStatus.SUCCESS, result.getStatus());
            }


            verify(transferService).transferMoney(any(Wallet.class), any(Wallet.class),
                    anyLong());
            verify(orderRepository).save(any(Order.class));
        }

        @Test
        void pay_InvalidPaymentService_ThrowsException() {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setSourceId("1");
            paymentDto.setOrderId("123");
            paymentDto.setPaymentService(null);

            assertThrows(RuntimeException.class, () -> paymentService.pay(paymentDto));
        }
    }

    private Partner getPartner() {
        Partner partner = new Partner();
        partner.setId("1");
        return partner;
    }

    private WalletTransaction getWalletTransaction() {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setTransaction(new Transaction());
        return walletTransaction;
    }
}
