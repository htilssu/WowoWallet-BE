package com.wowo.wowo.contexts.wallet.presentation.rest

import com.wowo.wowo.contexts.wallet.application.dto.*
import com.wowo.wowo.contexts.wallet.application.usecase.*
import com.wowo.wowo.shared.domain.OwnerType
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import org.springframework.http.*
import org.springframework.security.access.prepost.*
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for Wallet operations
 */
@RestController
@RequestMapping("/wallets")
class WalletController(
    private val createWalletUseCase: CreateWalletUseCase, private val creditWalletUseCase: CreditWalletUseCase
) {

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ApiResponse(
        responseCode = "201", description = "Wallet created successfully", content = [Content(
            mediaType = "application/json", schema = Schema(implementation = WalletDTO::class)
        )]
    )
    fun createWallet(@RequestBody request: CreateWalletRequest): ResponseEntity<WalletDTO> {
        val command = CreateWalletCommand(
            ownerId = request.ownerId, ownerType = request.ownerType, currency = request.currency
        )

        val walletDTO = createWalletUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(walletDTO)
    }

    @PostMapping("/{walletId}/credit")
    @PreAuthorize("isAuthenticated()")
    fun creditWallet(
        @PathVariable walletId: String, @RequestBody request: CreditWalletRequest
    ): ResponseEntity<WalletDTO> {
        val command = CreditWalletCommand(
            walletId = walletId, amount = request.amount, currency = request.currency
        )

        val walletDTO = creditWalletUseCase.execute(command)
        return ResponseEntity.ok(walletDTO)
    }
}

data class CreateWalletRequest(
    val ownerId: String, val ownerType: OwnerType, val currency: String
)

data class CreditWalletRequest(

    val amount: String, val currency: String
)

