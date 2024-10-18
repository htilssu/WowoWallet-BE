package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.request.CreateWalletDto;
import com.wowo.wowo.data.dto.response.WalletDto;
import com.wowo.wowo.data.dto.WalletResponse;
import com.wowo.wowo.models.Wallet;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    // Chuyển đổi từ Wallet sang WalletResponse
    WalletResponse toResponse(Wallet wallet);

    // Chuyển đổi từ CreateWalletDto sang Wallet
    Wallet toEntity(CreateWalletDto createWalletDto);

    // Chuyển đổi từ Wallet sang CreateWalletDto (nếu cần)
    WalletDto toDto(Wallet wallet);

    // Phương thức cập nhật (nếu cần)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Wallet partialUpdate(CreateWalletDto createWalletDto, @MappingTarget Wallet wallet);
}
