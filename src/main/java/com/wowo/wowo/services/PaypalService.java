package com.wowo.wowo.services;

import com.paypal.sdk.PaypalServerSDKClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import com.wowo.wowo.data.dto.TopUpRequestDto;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.mongo.repositories.TopUpRequestRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

@Service
@AllArgsConstructor
@Data
public class PaypalService {

    private final PaypalServerSDKClient paypalServerSDKClient;
    private final TopUpService topUpService;
    private final TopUpRequestRepository topUpRequestRepository;
    private final WalletService walletService;

    public Order createOrder() throws IOException, ApiException {
        final OrdersController ordersController = paypalServerSDKClient.getOrdersController();


        OrdersCreateInput ordersCreateInput = new OrdersCreateInput.Builder(
                null,
                new OrderRequest.Builder(
                        CheckoutPaymentIntent.CAPTURE,
                        Collections.singletonList(
                                new PurchaseUnitRequest.Builder(
                                        new AmountWithBreakdown.Builder(
                                                "USD",
                                                "100000"
                                        ).build()
                                ).build()
                        )
                ).build()
        ).prefer("return=representation")
                .build();

        final ApiResponse<Order> orderApiResponse = ordersController.ordersCreate(
                ordersCreateInput);
        return orderApiResponse.getResult();
    }

    public Order createTopUpOrder(TopUpRequestDto topUpRequestDto) throws IOException,
                                                                          ApiException {
        final OrdersController ordersController = paypalServerSDKClient.getOrdersController();


        OrdersCreateInput ordersCreateInput = new OrdersCreateInput.Builder(
                null,
                new OrderRequest.Builder(
                        CheckoutPaymentIntent.CAPTURE,
                        Collections.singletonList(
                                new PurchaseUnitRequest.Builder(
                                        new AmountWithBreakdown.Builder(
                                                "USD",
                                                BigDecimal.valueOf(
                                                                topUpRequestDto.getAmount() / 23000D)
                                                        .setScale(2,
                                                                RoundingMode.HALF_UP).toString()
                                        ).build()
                                ).build()
                        )
                ).applicationContext(new OrderApplicationContext.Builder().returnUrl(
                        "https://wowo.htilssu.id.vn/home").build()
                ).build()
        ).prefer("return=representation")
                .build();
        final ApiResponse<Order> orderApiResponse = ordersController.ordersCreate(
                ordersCreateInput);
        if (orderApiResponse.getStatusCode() == 201) {
            return orderApiResponse.getResult();
        }

        return null;
    }

    public Wallet captureOrder(String orderId) throws IOException, ApiException {
        final OrdersController ordersController = paypalServerSDKClient.getOrdersController();
        final ApiResponse<Order> orderApiResponse = ordersController.ordersCapture(
                new OrdersCaptureInput.Builder()
                        .id(orderId)
                        .prefer("return=minimal")
                        .build()
        );

        if (orderApiResponse.getStatusCode() == 200) {
            System.out.println("Capture order success");
            return topUpService.topUp(orderApiResponse.getResult());
        }
        else {
            System.out.println("Capture order failed");
        }
        return null;
    }
}
