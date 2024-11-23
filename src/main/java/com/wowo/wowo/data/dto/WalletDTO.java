package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    @NotNull
    private String ownerType;

    @NotNull
    private String currency;

    @Size(max = 10)
    private String ownerId;

    @NotNull
    private Double balance;
}
