package com.wowo.wowo.contexts.wallet.application.dto

import com.wowo.wowo.shared.domain.OwnerType

data class CreateWalletCommand(
    val ownerId: String,
    val ownerType: OwnerType,
    val currency: String
)


data class CreditWalletCommand(
    val walletId: String,
    val amount: String,
    val currency: String
)

data class DebitWalletCommand(
    val walletId: String,
    val amount: String,
    val currency: String
)

