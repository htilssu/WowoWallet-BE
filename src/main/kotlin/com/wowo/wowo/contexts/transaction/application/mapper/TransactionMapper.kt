package com.wowo.wowo.contexts.transaction.application.mapper

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * Mapper for converting Transaction domain entity to DTO.
 *
 * Creates unenriched DTOs - enrichment is handled separately
 * by TransactionOwnerEnricher.
 */
@Mapper(componentModel = "spring")
interface TransactionMapper {
    /**
     * Convert Transaction entity to DTO.
     * The owner fields (sourceOwnerId, sourceOwnerType, sourceOwnerName, etc.)
     * will be null and should be enriched separately.
     */
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "sourceWalletId", source = "sourceWalletId")
    @Mapping(target = "sourceOwnerId", ignore = true)
    @Mapping(target = "sourceOwnerType", ignore = true)
    @Mapping(target = "sourceOwnerName", ignore = true)
    @Mapping(target = "targetWalletId", source = "targetWalletId")
    @Mapping(target = "targetOwnerId", ignore = true)
    @Mapping(target = "targetOwnerType", ignore = true)
    @Mapping(target = "targetOwnerName", ignore = true)
    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", expression = "java(transaction.getStatus().name())")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "normalizedDescription", ignore = true)
    @Mapping(target = "transactionCategory", ignore = true)
    @Mapping(target = "merchantName", ignore = true)
    @Mapping(target = "merchantCategory", ignore = true)
    @Mapping(target = "geoCountry", ignore = true)
    @Mapping(target = "geoCity", ignore = true)
    @Mapping(target = "riskScore", ignore = true)
    @Mapping(target = "riskLevel", ignore = true)
    @Mapping(target = "tags", expression = "java(java.util.Collections.emptyList())")
    fun toDTO(transaction: Transaction): TransactionDTO

    fun toDTOs(transactions: List<Transaction>): List<TransactionDTO>
}
