package com.wowo.wowo.shared.presentation.rest

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

class PaginationDto {
    @Min(0, message = "Page must be greater than or equal to 0") var page: Int = 0

    @Max(100, message = "Max value is 100") var size: Int = 10
}