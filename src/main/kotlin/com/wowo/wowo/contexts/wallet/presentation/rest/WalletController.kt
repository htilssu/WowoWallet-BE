package com.wowo.wowo.contexts.wallet.presentation.rest

import com.wowo.wowo.contexts.wallet.application.dto.CreateWalletCommand
import com.wowo.wowo.contexts.wallet.application.dto.CreditWalletCommand
import com.wowo.wowo.contexts.wallet.application.dto.WalletDTO
import com.wowo.wowo.contexts.wallet.application.usecase.CreateWalletUseCase
import com.wowo.wowo.contexts.wallet.application.usecase.CreditWalletUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for Wallet operations
 */
@RestController
@RequestMapping("/wallets")
class WalletController(
    private val createWalletUseCase: CreateWalletUseCase,
    private val creditWalletUseCase: CreditWalletUseCase
) {

    @PostMapping
    fun createWallet(@RequestBody request: CreateWalletRequest): ResponseEntity<WalletDTO> {
        val command = CreateWalletCommand(
            userId = request.userId,
            currency = request.currency
        )

        val walletDTO = createWalletUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(walletDTO)
    }

    @PostMapping("/{walletId}/credit")
    fun creditWallet(
        @PathVariable walletId: String,
        @RequestBody request: CreditWalletRequest
    ): ResponseEntity<WalletDTO> {
        val command = CreditWalletCommand(
            walletId = walletId,
            amount = request.amount,
            currency = request.currency
        )

        val walletDTO = creditWalletUseCase.execute(command)
        return ResponseEntity.ok(walletDTO)
    }
}

data class CreateWalletRequest(
    val userId: String,
    val currency: String
)

data class CreditWalletRequest(
    val amount: String,
    val currency: String
)

