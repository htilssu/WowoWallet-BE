package com.wowo.wowo.contexts.transaction.application.mapper

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.dto.MoneyDTO
import com.wowo.wowo.contexts.transaction.application.dto.PartyDTO
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TransactionMapper {
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "sender", expression = "java(new PartyDTO(transaction.getSourceWalletId(), null, null, null))")
    @Mapping(target = "receiver", expression = "java(new PartyDTO(transaction.getTargetWalletId(), null, null, null))")
    @Mapping(target = "amount", expression = "java(new MoneyDTO(transaction.getAmount().getAmount(), transaction.getAmount().getCurrency().name()))")
    @Mapping(target = "sourceOwnerId", ignore = true)
    @Mapping(target = "sourceOwnerType", ignore = true)
    @Mapping(target = "sourceOwnerName", ignore = true)
    @Mapping(target = "targetOwnerId", ignore = true)
    @Mapping(target = "targetOwnerType", ignore = true)
    @Mapping(target = "targetOwnerName", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "risk", ignore = true)
    @Mapping(target = "tags", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "metadata", expression = "java(java.util.Collections.emptyMap())")
    fun toDTO(transaction: Transaction): TransactionDTO

    fun toDTOs(transactions: List<Transaction>): List<TransactionDTO>
}
