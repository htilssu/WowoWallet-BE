package com.wowo.wowo.designPattern.ChainOfResponsibility.Handle;

import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import lombok.Data;

@Data
public class TransferRequest {
    private Wallet source;
    private Wallet destination;
    private long amount;
    private String message;
    private Transaction transaction; // Dùng để lưu thông tin giao dịch trong quá trình xử lý

    public TransferRequest(Wallet source, Wallet destination, long amount, String message) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.message = message;
    }
}