package com.wowo.wowo.contexts.transaction.application.dto

data class TransferMoneyCommand(
    val fromWalletId: String,
    val toWalletId: String,
    val amount: String,
    val currency: String,
    val description: String?
)

