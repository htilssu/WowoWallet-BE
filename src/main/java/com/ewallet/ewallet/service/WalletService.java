package com.ewallet.ewallet.service;

import com.ewallet.ewallet.data.mapper.WalletMapper;
import com.ewallet.ewallet.data.dto.response.WalletResponse;
import com.ewallet.ewallet.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletMapper walletMapper;
    private final WalletRepository walletRepository;

    public WalletResponse getWallet(int id) {
        var wallet = walletRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));


        return walletMapper.toResponse(wallet);
    }
}
