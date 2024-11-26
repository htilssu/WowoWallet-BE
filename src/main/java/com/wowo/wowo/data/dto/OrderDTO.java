package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.Voucher;
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
public class OrderDTO implements Serializable {

    private Collection<OrderItemCreationDTO> items;
    private Collection<Voucher> vouchers;
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
    private Long discountMoney;
    private String checkoutUrl;
    private ApplicationDto application;


    /**
     * DTO for {@link com.wowo.wowo.model.Application}
     */
    public record ApplicationDto(Long id, String name) implements Serializable {

    }
}