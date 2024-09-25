package com.wowo.wowo.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WalletResponse {

    private int id;
    private String ownerId;
    private String ownerType;
    private double balance;
    private String currency;

}
