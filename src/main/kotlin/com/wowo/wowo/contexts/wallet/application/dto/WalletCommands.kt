package com.wowo.wowo.contexts.wallet.application.dto

data class CreateWalletCommand(
    val userId: String,
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

