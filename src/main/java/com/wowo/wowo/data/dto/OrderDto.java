package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wowo.wowo.model.PartnerStatus;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.Voucher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * DTO for {@link com.wowo.wowo.model.Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Serializable {

    private Long id;
    @NotNull
    private Long money;
    @NotNull
    private PaymentStatus status;
    @Size(max = 300)
    private String returnUrl;
    @Size(max = 300)
    private String successUrl;
    private String created;
    private String updated;
    @Size(max = 100)
    private String serviceName;
    Collection<OrderItemCreateDto> items;
    Collection<Voucher> vouchers;
    private String checkoutUrl;
    private PartnerDto partner;
    private Long discountMoney;


    /**
     * DTO for {@link com.wowo.wowo.model.Partner}
     */
    public record PartnerDto(@Size(max = 32) String id, String description, String name,
                             @NotNull @Email String email, PartnerStatus status)
            implements Serializable {

    }
}