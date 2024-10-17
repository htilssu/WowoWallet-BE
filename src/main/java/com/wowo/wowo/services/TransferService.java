package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    private final WalletRepository walletRepository;

    @Transactional
    public ResponseEntity<?> transfer(TransferDto data) {

        Wallet senderWallet = null;
        if (data.getSourceId() == null) {
            senderWallet = walletRepository.findByOwnerId(data.getSenderId())
                    .orElseThrow(
                            () -> new NotFoundException("Không tìm thấy ví"));
        }

        var receiverWallet = walletRepository.findByOwnerId(data.getReceiverId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví người nhận"));

        if (senderWallet != null) {
            transferMoney(senderWallet, receiverWallet, data.getMoney());
        }
        return ResponseEntity.ok().build();
    }

    protected void transferMoney(Wallet source, Wallet destination, double amount) {
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        walletRepository.saveAll(List.of(source, destination));
    }
}
