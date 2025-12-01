package com.wowo.wowo.presentation.rest

import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.usecase.TransferMoneyUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for Transaction operations
 */
@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val transferMoneyUseCase: TransferMoneyUseCase
) {

    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated()")
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

