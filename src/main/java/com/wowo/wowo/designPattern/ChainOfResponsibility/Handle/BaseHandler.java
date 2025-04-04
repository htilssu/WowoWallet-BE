package com.wowo.wowo.designPattern.ChainOfResponsibility.Handle;

import com.wowo.wowo.model.Transaction;

public abstract class BaseHandler implements Handler {
    private Handler next;

    @Override
    public void setNext(Handler h) {
        this.next = h;
    }

    @Override
    public Transaction handle(TransferRequest request) {
        if (next != null) {
            return next.handle(request);
        }
        return null; // Nếu không có handler tiếp theo, trả về null (có thể tùy chỉnh)
    }
}
