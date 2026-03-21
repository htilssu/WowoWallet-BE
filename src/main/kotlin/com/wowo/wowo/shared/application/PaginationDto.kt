package com.wowo.wowo.shared.application

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PaginationDto(
    @param:Min(0, message = "Page must be greater than or equal to 0") var page: Int = 0,

    @param:Max(100, message = "Max value is 100") var size: Int = 10
)