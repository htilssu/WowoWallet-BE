package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin cơ bản của giao dịch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInfo {

    /**
     * ID của giao dịch
     */
    private String transactionId;
}