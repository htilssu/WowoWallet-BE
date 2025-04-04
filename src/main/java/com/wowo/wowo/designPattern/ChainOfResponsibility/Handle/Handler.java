package com.wowo.wowo.designPattern.ChainOfResponsibility.Handle;

import com.wowo.wowo.model.Transaction;

public interface Handler {
    void setNext(Handler h);
    Transaction handle(TransferRequest request);
}
