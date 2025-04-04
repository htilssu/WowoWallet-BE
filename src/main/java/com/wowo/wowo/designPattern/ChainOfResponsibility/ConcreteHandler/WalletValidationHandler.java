package com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler;

import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.BaseHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.TransferRequest;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class WalletValidationHandler extends BaseHandler {
    @Override
    public Transaction handle(TransferRequest request) {
        if (request.getSource().getId().equals(request.getDestination().getId())) {
            throw new BadRequest("Không thể chuyển tiền từ ví này đến chính ví này");
        }
        return super.handle(request); // Chuyển tiếp nếu hợp lệ
    }
}
