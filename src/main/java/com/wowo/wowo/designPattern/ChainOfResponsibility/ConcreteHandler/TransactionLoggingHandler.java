package com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler;

import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.BaseHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.TransferRequest;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TransactionLoggingHandler extends BaseHandler {

    //Tạo và lưu giao dịch.
    private final TransactionService transactionService;

    @Autowired
    public TransactionLoggingHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Transaction handle(TransferRequest request) {
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .receiveWallet(request.getDestination())
                .senderWallet(request.getSource())
                .message(request.getMessage() != null ? request.getMessage() : "Chuyển tiền")
                .flowType(FlowType.TRANSFER_MONEY)
                .created(Instant.now())
                .updated(Instant.now())
                .build();

        request.setTransaction(transaction); // Lưu transaction vào request để các handler sau có thể sử dụng nếu cần
        return transactionService.save(transaction); // Lưu giao dịch và trả về
    }
}
