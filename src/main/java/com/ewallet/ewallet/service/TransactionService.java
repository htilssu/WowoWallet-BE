package com.ewallet.ewallet.service;

import com.ewallet.ewallet.data.mapper.TransactionMapper;
import com.ewallet.ewallet.data.dto.response.WalletTransactionDto;
import com.ewallet.ewallet.data.mapper.WalletTransactionMapperImpl;
import com.ewallet.ewallet.models.PaymentRequest;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.models.Wallet;
import com.ewallet.ewallet.repositories.PaymentRequestRepository;
import com.ewallet.ewallet.repositories.TransactionRepository;
import com.ewallet.ewallet.repositories.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletTransactionService walletTransactionService;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionMapperImpl walletTransactionMapperImpl;
    private final PaymentRequestRepository paymentRequestRepository;

    @Transactional(readOnly = true)
    public WalletTransactionDto getTransactionDetail(String id) {
        var transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }

        var transactionResponse = transactionMapper.toResponse(transaction);

        if ("wallet".equals(transaction.getTransactionTarget())) {
            var walletTransaction = walletTransactionRepository.findById(transaction.getId()).get();

            return walletTransactionMapperImpl.toDto(walletTransaction);
        }
        // TODO: Xử lý các loại giao dịch khác nếu cần

        return null;
    }

    public void refund(Transaction transaction) {
        if (transaction.getTransactionTarget().equals("wallet")) {
            walletTransactionService.refund(transaction);
        }
        else {
            throw new RuntimeException("Transaction target not found");
        }
    }

    public Transaction createTransaction(String userId,
            PaymentRequest paymentRequest,
            Wallet sender,
            Wallet receiver) {
        final Transaction transaction = createTransaction(userId, paymentRequest);

        if (paymentRequest.getStatus().equals("SUCCESS")) {
            walletTransactionService.createWalletTransaction(transaction, sender, receiver);
        }

        paymentRequest.setTransaction(transaction);
        paymentRequestRepository.save(paymentRequest);
        return transaction;
    }

    public Transaction createTransaction(String userId, PaymentRequest paymentRequest) {
        var transaction = new Transaction();
        transaction.setSenderId(userId);
        transaction.setSenderType("user");
        transaction.setReceiverId(paymentRequest.getPartner().getId());
        transaction.setReceiverType("partner");
        transaction.setMoney(paymentRequest.getMoney());
        transaction.setTransactionTarget("wallet");
        transaction.setStatus(paymentRequest.getStatus());

        transactionRepository.save(transaction);


        return transaction;
    }
}
