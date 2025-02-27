package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.WalletCreationDTO;
import com.wowo.wowo.data.dto.WalletDTO;
import com.wowo.wowo.model.UserWallet;
import com.wowo.wowo.model.Wallet;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    Wallet toEntity(WalletCreationDTO walletCreationDTO);

    WalletDTO toDto(Wallet userWallet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Wallet partialUpdate(WalletCreationDTO walletCreationDTO,
            @MappingTarget Wallet userWallet);
}
