package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.WalletTransactionDto;
import com.wowo.wowo.models.WalletTransaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {TransactionMapper.class})
public interface WalletTransactionMapper {

    @Mapping(source = "receiverWalletOwnerId", target = "receiverWallet.ownerId")
    @Mapping(source = "receiverWalletCurrency", target = "receiverWallet.currency")
    @Mapping(source = "receiverWalletOwnerType", target = "receiverWallet.ownerType")
    @Mapping(source = "receiverWalletId", target = "receiverWallet.id")
    @Mapping(source = "senderWalletBalance", target = "senderWallet.balance")
    @Mapping(source = "senderWalletOwnerId", target = "senderWallet.ownerId")
    @Mapping(source = "senderWalletCurrency", target = "senderWallet.currency")
    @Mapping(source = "senderWalletOwnerType", target = "senderWallet.ownerType")
    @Mapping(source = "senderWalletId", target = "senderWallet.id")
    WalletTransaction toEntity(WalletTransactionDto walletTransactionDto);
    @InheritInverseConfiguration(
            name = "toEntity")

    WalletTransactionDto toDto(WalletTransaction walletTransaction);
    @InheritConfiguration(name = "toEntity")
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    WalletTransaction partialUpdate(
            WalletTransactionDto walletTransactionDto,
            @MappingTarget WalletTransaction walletTransaction);
}