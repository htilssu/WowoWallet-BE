package com.wowo.wowo.contexts.transaction.application.mapper

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import org.springframework.stereotype.Component

/**
 * Mapper for converting Transaction domain entity to DTO.
 * 
 * Creates unenriched DTOs - enrichment is handled separately
 * by TransactionOwnerEnricher.
 */
@Component
class TransactionMapper {
    
    /**
     * Convert Transaction entity to DTO.
     * The owner fields (fromOwnerId, fromOwnerType, fromOwnerName, etc.)
     * will be null and should be enriched separately.
     */
    fun toDTO(transaction: Transaction): TransactionDTO {
        return TransactionDTO.unenriched(
            id = transaction.id.value,
            fromWalletId = transaction.fromWalletId,
            toWalletId = transaction.toWalletId,
            amount = transaction.amount.amount,
            currency = transaction.amount.currency.code,
            type = transaction.type.name,
            status = transaction.getStatus().name,
            description = transaction.description,
            reference = transaction.reference,
            createdAt = transaction.createdAt,
            updatedAt = transaction.updatedAt
        )
    }
    
    /**
     * Convert a list of Transaction entities to DTOs.
     */
    fun toDTOs(transactions: List<Transaction>): List<TransactionDTO> {
        return transactions.map { toDTO(it) }
    }
}
