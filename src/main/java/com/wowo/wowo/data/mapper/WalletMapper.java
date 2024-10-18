package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.WalletResponse;
import com.wowo.wowo.models.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponse toResponse(Wallet wallet);
}
