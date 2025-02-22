package com.wowo.wowo.service;

import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.UserWallet;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.ApplicationPartnerWalletRepository;
import com.wowo.wowo.repository.UserWalletRepository;
import com.wowo.wowo.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class WalletService {

    private final UserWalletRepository userWalletRepository;
    private final WalletRepository walletRepository;
    private final ApplicationPartnerWalletRepository applicationPartnerWalletRepository;

    public UserWallet getUserWallet(String userId) {
        return userWalletRepository.findUserWalletByUser_Id(userId);
    }

    public Wallet plusBalance(String walletId, Long amount) {
        final Wallet userWallet = getWallet(Long.parseLong(walletId));
        if (amount < 0 && userWallet.getBalance() < Math.abs(amount)) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        userWallet.setBalance(userWallet.getBalance() + amount);
        return walletRepository.save(userWallet);
    }

    public Wallet getWallet(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ví"));

    }

    public Wallet createWallet() {
        return walletRepository.save(new Wallet());
    }

    public Wallet getRootWallet() {
        return walletRepository.findById(-1L)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ví"));
    }

    public void save(Wallet... rootWallet) {
        walletRepository.saveAll(Arrays.asList(rootWallet));
    }

    public Wallet getWallet(Authentication authentication) {
        //        TODO: implement
        throw new NotImplementedException("Chưa implement");
    }

    public void deleteWallet(Long id) {
        walletRepository.deleteById(id);
    }
}
