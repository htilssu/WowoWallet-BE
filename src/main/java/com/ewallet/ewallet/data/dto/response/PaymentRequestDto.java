package com.ewallet.ewallet.data.dto.response;

import com.ewallet.ewallet.models.PaymentRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link PaymentRequest}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequestDto implements Serializable {

    @Size(max = 15)
    private String id;
    @NotNull
    private double money;
    @Size(max = 50)
    private String status;
    private TransactionResponse transaction;
    @Size(max = 50)
    private String voucherId;
    @Size(max = 100)
    private String voucherName;
    @Size(max = 100)
    private String voucherCode;
    @Size(max = 50)
    private String orderId;
    @Size(max = 300)
    private String returnUrl;
    private double voucherDiscount;
    @Size(max = 50)
    private String externalTransactionId;
    private String created;
    private String updated;
    private String partnerId;
    private String partnerName;
    private String partnerEmail;
    private String partnerPartnerType;
    private String partnerAvatar;
    @Size(max = 100)
    private String serviceName;
}