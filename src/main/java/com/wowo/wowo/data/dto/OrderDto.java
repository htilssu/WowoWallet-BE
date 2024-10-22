package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wowo.wowo.models.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

/**
 * DTO for {@link Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Serializable {

    Long id;
    @NotNull
    Long money;
    @NotNull
    PaymentStatus status;
    @Size(max = 300)
    String returnUrl;
    @Size(max = 300)
    String successUrl;
    String created;
    String updated;
    @Size(max = 100)
    String serviceName;
    Collection<OrderItemCreateDto> items;
}