package com.ewallet.ewallet.repositories;

import com.ewallet.ewallet.models.PaymentRequest;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRequestRepository extends CrudRepository<PaymentRequest,String> {

    PaymentRequest findByOrderId(String orderId);
}
