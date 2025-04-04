package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.UserWallet;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.TransactionRepository;
import com.wowo.wowo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletRepository walletRepository;
    private final WalletService walletService;

    @Override
    public double getTransactionAmount(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giao dịch với ID: " + transactionId));
        return transaction.getAmount();
    }

    @Override
    @Transactional
    public void refund(Transaction transaction) {
        // Hoàn tiền từ người nhận về người gửi
        Wallet receiverWallet = transaction.getReceiveWallet();
        Wallet senderWallet = transaction.getSenderWallet();

        // Trừ tiền từ ví người nhận
        receiverWallet.addBalance(-transaction.getAmount());

        // Cộng tiền vào ví người gửi
        senderWallet.addBalance(transaction.getAmount());

        // Lưu thay đổi vào cơ sở dữ liệu
        walletRepository.save(receiverWallet);
        walletRepository.save(senderWallet);

        // Tạo giao dịch hoàn tiền mới
        Transaction refundTransaction = new Transaction();
        refundTransaction.setAmount(transaction.getAmount());
        refundTransaction.setSenderWallet(receiverWallet);
        refundTransaction.setReceiveWallet(senderWallet);
        refundTransaction.setFlowType(FlowType.REFUND);
        refundTransaction.setMessage("Hoàn tiền cho giao dịch " + transaction.getId());
        refundTransaction.setReceiverName(senderWallet.toString());
        refundTransaction.setSenderName(receiverWallet.toString());

        // Lưu giao dịch hoàn tiền
        transactionRepository.save(refundTransaction);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> getRecentTransactions(String userId, int offset, int page) {
        // Lấy ví của người dùng
        UserWallet userWallet = walletService.getUserWallet(userId);
        if (userWallet == null) {
            return new ArrayList<>();
        }

        // UserWallet là subclass của Wallet nên có thể dùng trực tiếp

        // Tạo Pageable để phân trang
        Pageable pageable = PageRequest.of(page, offset);

        // Lấy danh sách giao dịch
        List<Transaction> transactions = transactionRepository
                .findTransactionsByReceiveWalletOrSenderWalletOrderByUpdatedDesc(userWallet, userWallet, pageable);

        // Chuyển đổi sang DTO và format thời gian
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        for (Transaction transaction : transactions) {
            TransactionDTO dto = transactionMapper.toDto(transaction);
            dto.setCreated(formatter.format(transaction.getCreated()));
            dto.setUpdated(formatter.format(transaction.getUpdated()));
            transactionDTOs.add(dto);
        }

        return transactionDTOs;
    }

    @Override
    public long getTotalTransactions(String userId) {
        UserWallet userWallet = walletService.getUserWallet(userId);
        if (userWallet == null) {
            return 0;
        }

        // UserWallet kế thừa từ Wallet
        return transactionRepository.countTransactionBySenderWalletOrReceiveWallet(userWallet, userWallet);
    }

    @Override
    public TransactionDTO getTransactionDetail(String id, Authentication authentication) {
        // Lấy thông tin người dùng từ authentication
        String userId = authentication.getPrincipal().toString();

        // Lấy ví của người dùng
        UserWallet userWallet = walletService.getUserWallet(userId);
        if (userWallet == null) {
            throw new NotFoundException("Không tìm thấy ví của người dùng");
        }

        // Lấy giao dịch từ repository
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giao dịch với ID: " + id));

        // Kiểm tra quyền truy cập (userWallet kế thừa từ Wallet)
        if (!transaction.getSenderWallet().equals(userWallet) && !transaction.getReceiveWallet().equals(userWallet)) {
            throw new AccessDeniedException("Không có quyền xem giao dịch này");
        }

        // Chuyển đổi sang DTO và format thời gian
        TransactionDTO dto = transactionMapper.toDto(transaction);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        dto.setCreated(formatter.format(transaction.getCreated()));
        dto.setUpdated(formatter.format(transaction.getUpdated()));

        return dto;
    }

    @Override
    public List<Transaction> getGroupFundTransaction(Long groupId, Integer offset, Integer page) {
        Pageable pageable = PageRequest.of(page, offset);
        return transactionRepository.getGroupFundTransaction(groupId, pageable);
    }

    @Override
    public List<Map<String, Object>> getTransactionStats() {
        List<Object[]> statsData = transactionRepository.getTransactionStats();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : statsData) {
            Map<String, Object> statMap = new HashMap<>();
            statMap.put("totalTransactions", row[0]);
            statMap.put("totalAmount", row[1]);
            statMap.put("flowType", row[2]);
            statMap.put("totalTransfer", row[3]);
            statMap.put("totalReceive", row[4]);
            statMap.put("totalTopUp", row[5]);
            statMap.put("totalWithdraw", row[6]);
            statMap.put("totalTopUpGroupFund", row[7]);
            statMap.put("totalWithdrawGroupFund", row[8]);
            result.add(statMap);
        }

        return result;
    }
}