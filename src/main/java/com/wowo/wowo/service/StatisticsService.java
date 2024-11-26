package com.wowo.wowo.service;

import com.wowo.wowo.repository.ApplicationRepository;
import com.wowo.wowo.repository.GroupFundRepository;
import com.wowo.wowo.repository.UserRepository;
import com.wowo.wowo.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final WalletRepository walletRepository;

    private final GroupFundRepository groupFundRepository;

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

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

    public long getTotalApplication() {
        return applicationRepository.count();
    }

    // Phương thức trả về tổng số Partner
}

