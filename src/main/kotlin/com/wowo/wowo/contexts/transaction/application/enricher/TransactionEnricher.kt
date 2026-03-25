package com.wowo.wowo.contexts.transaction.application.enricher

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId
import com.wowo.wowo.contexts.wallet.infrastructure.persistence.WalletRepositoryAdapter
import com.wowo.wowo.shared.enrichment.IEnricher
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TransactionEnricher(val walletRepositoryAdapter: WalletRepositoryAdapter) : IEnricher<TransactionDTO> {
    override fun enrich(data: TransactionDTO): TransactionDTO {
        val sourceWallet = walletRepositoryAdapter.findById(WalletId(UUID.fromString(data.sourceWalletId)))
        val targetWallet = walletRepositoryAdapter.findById(WalletId(UUID.fromString(data.targetWalletId)))

        if (sourceWallet == null || targetWallet == null) {
            throw IllegalArgumentException("Source or Target wallet not found for transaction id '${data.sourceWalletId}'")
        }

        data.sourceOwnerId = sourceWallet.ownerId
        data.sourceOwnerType = sourceWallet.ownerType

        data.targetOwnerId = targetWallet.ownerId
        data.targetOwnerType = targetWallet.ownerType


        return data
    }

    override suspend fun enrichMany(data: List<TransactionDTO>): List<TransactionDTO> {


        return data
    }
}