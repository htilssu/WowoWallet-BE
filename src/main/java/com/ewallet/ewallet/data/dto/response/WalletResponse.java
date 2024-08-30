package com.ewallet.ewallet.data.dto.response;

import com.ewallet.ewallet.models.Partner;
import com.ewallet.ewallet.models.User;
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
