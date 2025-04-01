package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.TransactionRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface định nghĩa các dịch vụ liên quan đến giao dịch
 */
public interface TransactionService {

    /**
     * Lấy số tiền của giao dịch từ ID giao dịch
     * 
     * @param transactionId ID của giao dịch cần lấy thông tin
     * @return số tiền của giao dịch
     * @throws IllegalArgumentException nếu không tìm thấy giao dịch
     */
    double getTransactionAmount(String transactionId);

    void refund(Transaction transaction);

    Transaction save(Transaction transaction);

    List<TransactionDTO> getRecentTransactions(String userId, int offset, int page);

    long getTotalTransactions(String userId);

    TransactionDTO getTransactionDetail(String id, Authentication authentication);

    List<Transaction> getGroupFundTransaction(Long groupId,
            @Min(0) @NotNull Integer offset,
            @Min(0) @NotNull Integer page);

    // Thống kê
    List<Map<String, Object>> getTransactionStats();
}
