package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Serializable {

    @Size(max = 15)
    String id;
    @NotNull
    BigDecimal money;
    @Size(max = 50)
    String status;
    @Size(max = 300)
    String returnUrl;
    @Size(max = 300)
    String successUrl;
    Instant created;
    Instant updated;
    @Size(max = 100)
    String serviceName;
}