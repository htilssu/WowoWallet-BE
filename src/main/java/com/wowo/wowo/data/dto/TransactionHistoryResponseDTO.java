package com.wowo.wowo.data.dto;

import java.util.List;

public record TransactionHistoryResponseDTO(List<TransactionDTO> data, long total) {

}
