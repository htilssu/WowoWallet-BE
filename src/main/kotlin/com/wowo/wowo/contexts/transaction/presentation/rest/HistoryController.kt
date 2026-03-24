package com.wowo.wowo.contexts.transaction.presentation.rest

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.usecase.GetTransactionHistoryUseCase
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionSearchCriteria
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.contexts.transaction.domain.acl.GroupFundACL
import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.shared.domain.PagedResult
import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.shared.domain.OwnerType
import com.wowo.wowo.shared.infrastructure.security.SecurityUtils
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.infrastructure.security.annotations.RequireAuthenticated
import com.wowo.wowo.shared.application.PaginationDto
import org.springdoc.core.annotations.ParameterObject
import org.springframework.security.access.AccessDeniedException
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/history")
class HistoryController(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val walletACL: WalletACL,
    private val groupFundACL: GroupFundACL,
    private val securityUtils: SecurityUtils
) {

    @GetMapping
    @RequireAuthenticated
    fun getTransactionHistory(
        @RequestParam walletId: String,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: Instant?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: Instant?,
        @RequestParam(required = false) type: TransactionType?,
        @ModelAttribute paging: PaginationDto?
    ): ResponseEntity<PagedResult<TransactionDTO>> {
        val currentUserId = securityUtils.getCurrentUserId() ?: throw AccessDeniedException("User is not authenticated")

        val wallet =
            walletACL.getWallet(walletId) as? Wallet ?: throw EntityNotFoundException("Wallet not found: $walletId")

        val hasAccess = when (wallet.ownerType) {
            OwnerType.USER -> wallet.ownerId == currentUserId
            OwnerType.FUND_GROUP -> groupFundACL.canAccessFundWallet(wallet.ownerId, currentUserId)
            else -> false
        }

        if (!hasAccess) {
            throw AccessDeniedException("Access denied for wallet: $walletId")
        }

        val criteria = TransactionSearchCriteria(
            walletId = walletId,
            startDate = startDate,
            endDate = endDate,
            type = type,
            page = paging?.page ?: 1,
            size = paging?.size ?: 20
        )

        val result = getTransactionHistoryUseCase.execute(criteria)
        return ResponseEntity.ok(result)
    }
}
