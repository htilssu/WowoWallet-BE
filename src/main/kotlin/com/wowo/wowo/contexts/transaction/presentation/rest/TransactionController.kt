package com.wowo.wowo.contexts.transaction.presentation.rest

import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.usecase.TransferMoneyUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for Transaction operations
 */
@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transferMoneyUseCase: TransferMoneyUseCase
) {

    @PostMapping("/transfer")
    fun transferMoney(@RequestBody request: TransferMoneyRequest): ResponseEntity<TransactionDTO> {
        val command = TransferMoneyCommand(
            fromWalletId = request.fromWalletId,
            toWalletId = request.toWalletId,
            amount = request.amount,
            currency = request.currency,
            description = request.description
        )

        val transactionDTO = transferMoneyUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO)
    }
}

data class TransferMoneyRequest(
    val fromWalletId: String,
    val toWalletId: String,
    val amount: String,
    val currency: String,
    val description: String?
)

