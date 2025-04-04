package com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler;

import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.BaseHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.TransferRequest;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransferHandler extends BaseHandler {

    //Thực hiện chuyển tiền và cập nhật số dư.
    private final WalletRepository walletRepository;

    @Autowired
    public TransferHandler(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Transaction handle(TransferRequest request) {
        Wallet source = request.getSource();
        Wallet destination = request.getDestination();
        long amount = request.getAmount();

        // Thực hiện chuyển tiền
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        walletRepository.saveAll(List.of(source, destination));

        return super.handle(request); // Chuyển tiếp
    }
}
