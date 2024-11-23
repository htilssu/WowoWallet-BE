package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.request.CreateWalletDTO;
import com.wowo.wowo.data.dto.*;
import com.wowo.wowo.data.dto.WalletResponse;
import com.wowo.wowo.model.Wallet;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    // Chuyển đổi từ Wallet sang WalletResponse
    WalletResponse toResponse(Wallet wallet);

    // Chuyển đổi từ CreateWalletDTO sang Wallet
    Wallet toEntity(CreateWalletDTO createWalletDTO);

    // Chuyển đổi từ Wallet sang CreateWalletDTO (nếu cần)
    WalletDTO toDTO(Wallet wallet);

    // Phương thức cập nhật (nếu cần)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Wallet partialUpdate(CreateWalletDTO createWalletDTO, @MappingTarget Wallet wallet);
}
