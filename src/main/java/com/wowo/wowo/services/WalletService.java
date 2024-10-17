package com.wowo.wowo.services;

import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.data.dto.WalletResponse;
import com.wowo.wowo.repositories.WalletRepository;
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
