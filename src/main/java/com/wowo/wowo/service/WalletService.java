package com.wowo.wowo.service;

import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.model.Partner;
import com.wowo.wowo.model.User;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.model.WalletOwnerType;
import com.wowo.wowo.repository.WalletRepository;
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
        final Wallet wallet = getWallet(Integer.parseInt(walletId));
        if (amount < 0 && wallet.getBalance() < Math.abs(amount)) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        wallet.setBalance(wallet.getBalance() + amount);
        return walletRepository.save(wallet);
    }
}
