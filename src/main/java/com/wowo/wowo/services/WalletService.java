package com.wowo.wowo.services;

import com.wowo.wowo.models.Partner;
import com.wowo.wowo.models.User;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.models.WalletOwnerType;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWallet(int id) {
        return walletRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));

    }

    public Wallet createWallet(Partner partner) {
        Wallet wallet = new Wallet();
        wallet.setOwnerId(partner.getId());
        wallet.setOwnerType(WalletOwnerType.PARTNER);

        try {
            return walletRepository.save(wallet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setOwnerId(user.getId());
        wallet.setOwnerType(WalletOwnerType.USER);

        try {
            return walletRepository.save(wallet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWalletOwner(String userId, int walletId) {
        return walletRepository.existsByOwnerIdAndId(userId, (long) walletId);
    }

    public boolean isWalletOwner(String userId, String walletId) {
        final long walletIdInt = Long.parseLong(walletId);
        return walletRepository.existsByOwnerIdAndId(userId, walletIdInt);
    }

    public Optional<Wallet> getPartnerWallet(String partnerId) {
        return walletRepository.findByOwnerIdAndOwnerType(partnerId,
                WalletOwnerType.PARTNER);
    }

    public Optional<Wallet> getUserWallet(String ownerId) {
        return walletRepository.findByOwnerIdAndOwnerType(ownerId, WalletOwnerType.USER);
    }

    public Wallet plusBalance(String walletId, Long amount) {
        final Wallet wallet = getWallet(walletId);
        wallet.setBalance(wallet.getBalance() + amount);
        return walletRepository.save(wallet);
    }
}
