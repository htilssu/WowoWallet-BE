package com.wowo.wowo.contexts.transaction.presentation.rest

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.usecase.GetTransactionHistoryUseCase
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionSearchCriteria
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.shared.domain.PagedResult
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/history")
class HistoryController(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase
) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getTransactionHistory(
        @RequestParam walletId: String,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime?,
        @RequestParam(required = false) type: TransactionType?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PagedResult<TransactionDTO>> {
        // TODO: Validate that the current user owns the walletId or has permission to view it
        
        val criteria = TransactionSearchCriteria(
            walletId = walletId,
            startDate = startDate,
            endDate = endDate,
            type = type,
            page = page,
            size = size
        )

        val result = getTransactionHistoryUseCase.execute(criteria)
        return ResponseEntity.ok(result)
    }
}
