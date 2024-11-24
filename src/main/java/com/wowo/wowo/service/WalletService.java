package com.wowo.wowo.service;

import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.UserWalletRepository;
import com.wowo.wowo.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final UserWalletRepository userWalletRepository;
    private final WalletRepository walletRepository;

    public UserWallet createWallet(Partner partner) {
        UserWallet userWallet = new UserWallet();
        userWallet.setOwnerId(partner.getId());
        userWallet.setOwnerType(WalletOwnerType.PARTNER);

        try {
            return userWalletRepository.save(userWallet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserWallet createWallet(User user) {
        UserWallet userWallet = new UserWallet();
        userWallet.setOwnerId(user.getId());
        userWallet.setOwnerType(WalletOwnerType.USER);

        try {
            return userWalletRepository.save(userWallet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWalletOwner(String userId, int walletId) {
        return userWalletRepository.existsByOwnerIdAndId(userId, (long) walletId);
    }

    public boolean isWalletOwner(String userId, String walletId) {
        final long walletIdInt = Long.parseLong(walletId);
        return userWalletRepository.existsByOwnerIdAndId(userId, walletIdInt);
    }

    public Optional<UserWallet> getPartnerWallet(String partnerId) {
        return userWalletRepository.findByOwnerIdAndOwnerType(partnerId,
                WalletOwnerType.PARTNER);
    }

    public Optional<UserWallet> getUserWallet(String ownerId) {
        return userWalletRepository.findByOwnerIdAndOwnerType(ownerId, WalletOwnerType.USER);
    }

    public UserWallet plusBalance(String walletId, Long amount) {
        final UserWallet userWallet = getWallet(Long.parseLong(walletId));
        if (amount < 0 && userWallet.getBalance() < Math.abs(amount)) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        userWallet.setBalance(userWallet.getBalance() + amount);
        return userWalletRepository.save(userWallet);
    }

    public UserWallet getWallet(Long id) {
        return userWalletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));

    }

    public UserWallet createWallet() {
        final UserWallet userWallet = userWalletRepository.save(new UserWallet());
        return userWallet;
    }

    public Wallet getRootWallet() {
        return walletRepository.findById(-1L)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ví"));
    }

    public void save(Wallet ...rootWallet) {
       walletRepository.saveAll(Arrays.asList(rootWallet));
    }

    public Wallet getWallet(Authentication authentication) {

    }
}
