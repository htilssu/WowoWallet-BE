package com.wowo.wowo.decorator;

import com.wowo.wowo.data.dto.WithdrawDTO;

public abstract class WithdrawServiceDecorator implements WithdrawService {
    protected final WithdrawService wrappedService;

    public WithdrawServiceDecorator(WithdrawService wrappedService) {
        this.wrappedService = wrappedService;
    }

    @Override
    public void withdraw(WithdrawDTO withdrawDTO) {
        wrappedService.withdraw(withdrawDTO); // Gọi đến service gốc
    }
}