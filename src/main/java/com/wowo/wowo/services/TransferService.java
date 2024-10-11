package com.wowo.wowo.services;

import com.wowo.wowo.data.vms.TransferVm;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.repositories.WalletRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final WalletRepository walletRepository;

    public TransferService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public ResponseEntity<?> transfer(TransferVm data) {

        if (data.getSourceId() == null) {
            var senderWallet = walletRepository.findByOwnerId(data.getSenderId());
        }

        var receiverWallet = walletRepository.findByOwnerIdAndOwnerType(data.getReceiverId(),
                "user").orElseThrow(() -> new NotFoundException("Không tìm thấy ví"));


        return ResponseEntity.ok().build();
    }
}
