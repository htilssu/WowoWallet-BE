package com.wowo.wowo.services;

import com.paypal.sdk.PaypalServerSDKClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Service
@AllArgsConstructor
@Data
public class PaypalService {

    private final PaypalServerSDKClient paypalServerSDKClient;

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
                                        )
                                                .build()
                                )
                                        .build()
                        )
                )
                        .build()
        )
                .prefer("return=representation")
                .build();

        final ApiResponse<Order> orderApiResponse = ordersController.ordersCreate(
                ordersCreateInput);
        final Order result = orderApiResponse.getResult();
       return result;
    }
}
