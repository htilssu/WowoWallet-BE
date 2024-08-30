package com.ewallet.ewallet.data.mapper;

import com.ewallet.ewallet.data.dto.response.WalletResponse;
import com.ewallet.ewallet.models.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponse toResponse(Wallet wallet);
}
