package com.wowo.wowo.shared.domain

data class PagedResult<T>(
    val items: List<T>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int
)
