package com.wowo.wowo.services;

import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.models.User;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletMapper walletMapper;
    private final WalletRepository walletRepository;

    public Wallet getWallet(int id) {
        return walletRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));


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

    public boolean isWalletOwner(String userId, int walletId) {
        return walletRepository.existsByOwnerIdAndId(userId, (long) walletId);
    }

    public boolean isWalletOwner(String userId, String walletId) {
        final Long walletIdInt = Long.valueOf(walletId);
        return walletRepository.existsByOwnerIdAndId(userId, (long) walletIdInt);
    }

    public Optional<Wallet> getPartnerWallet(String partnerId) {
        return walletRepository.findByOwnerIdAndOwnerType(partnerId,"partner");
    }
}
