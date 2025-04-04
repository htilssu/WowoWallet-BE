package com.wowo.wowo.decorator;

import com.wowo.wowo.data.dto.WithdrawDTO;
import com.wowo.wowo.service.TransferService;

public class FeeWithdrawDecorator extends WithdrawServiceDecorator {

    private final TransferService transferService;
    private final Long fee;

    public FeeWithdrawDecorator(WithdrawService wrappedService,
            TransferService transferService,
            Long fee) {
        super(wrappedService);
        this.transferService = transferService;
        this.fee = fee;
    }

    @Override
    public void withdraw(WithdrawDTO withdrawDTO) {
        Long totalAmount = withdrawDTO.getAmount() + fee;
        withdrawDTO.setAmount(totalAmount); // Cập nhật số tiền bao gồm phí
        super.withdraw(withdrawDTO);
    }
}