package com.wowo.wowo.data.dto;

import java.util.List;

public record TransactionHistoryResponseDto(List<TransactionDto> data, long total) {

}
