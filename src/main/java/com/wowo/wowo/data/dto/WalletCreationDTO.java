package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * DTO for creating a new Wallet
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class WalletCreationDTO {

    @NotNull
    @Size(max = 20)
    private String ownerType;

    @NotNull
    @Size(max = 3)
    private String currency;

    @Size(max = 10)
    private String ownerId;

    @NotNull
    private Double balance;
}

