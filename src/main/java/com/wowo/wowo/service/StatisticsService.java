package com.wowo.wowo.service;

import com.wowo.wowo.repositories.GroupFundRepository;
import com.wowo.wowo.repositories.PartnerRepository;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final WalletRepository walletRepository;

    private final GroupFundRepository groupFundRepository;

    private final UserRepository userRepository;

    private final PartnerRepository partnerRepository;

    // Phương thức trả về tổng số Wallet
    public long getTotalWallets() {
        return walletRepository.count();
    }

    // Phương thức trả về tổng số Group Fund
    public long getTotalGroupFunds() {
        return groupFundRepository.count();
    }

    // Phương thức trả về tổng số User
    public long getTotalUsers() {
        return userRepository.count();
    }

    // Phương thức trả về tổng số Partner
    public long getTotalPartners() {
        return partnerRepository.count();
    }
}

