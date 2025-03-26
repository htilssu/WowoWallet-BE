package com.wowo.wowo.Strategy;
import com.paypal.sdk.models.Order;
import com.wowo.wowo.data.dto.TopUpRequestDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.service.PaypalService;
import org.springframework.stereotype.Service;

@Service
public class PaypalPaymentStrategy implements PaymentStrategy {
    private final PaypalService paypalService;

    public PaypalPaymentStrategy(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    @Override
    public void processPayment(TopUpRequestDTO request) {
        try {
            Order order = paypalService.createTopUpOrder(request);
            String redirectUrl = order.getLinks().stream()
                    .filter(link -> "approve".equals(link.getRel()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequest("Bad request"))
                    .getHref();

            System.out.println("Redirect user to PayPal: " + redirectUrl);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý thanh toán PayPal", e);
        }
    }
}