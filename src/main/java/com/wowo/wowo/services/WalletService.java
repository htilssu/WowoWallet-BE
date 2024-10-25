package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.WalletResponse;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.models.User;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletMapper walletMapper;
    private final WalletRepository walletRepository;

    public WalletResponse getWallet(int id) {
        var wallet = walletRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));


        return walletMapper.toResponse(wallet);
    }

    public Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setOwnerId(user.getId());
        wallet.setOwnerType("user");

        try {
            wallet = walletRepository.save(wallet);
            return wallet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWalletOwner(String userId, String walletId) {
        final Long walletIdInt = Long.valueOf(walletId);
        return walletRepository.existsByOwnerIdAndId(userId, walletIdInt);
    }
}
