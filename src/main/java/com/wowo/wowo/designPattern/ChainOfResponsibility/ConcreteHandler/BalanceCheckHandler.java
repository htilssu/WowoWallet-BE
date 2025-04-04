package com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler;

import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.BaseHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.TransferRequest;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class BalanceCheckHandler extends BaseHandler {
    @Override
    public Transaction handle(TransferRequest request) {
        if (request.getAmount() <= 0) {
            throw new BadRequest("Số tiền chuyển phải lớn hơn 0");
        }
        if (request.getSource().getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        return super.handle(request); // Chuyển tiếp nếu số dư đủ
    }
}
