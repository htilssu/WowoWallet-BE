package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.StatisticSummary;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticsService {
    private final StatisticRepository statisticRepository;

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

    public Long getTotalMoneyByStatus(Long applicationId, PaymentStatus status) {
        return statisticRepository.getTotalMoneyByStatus(applicationId, status);
    }

    public Long getOrderCountByStatus(Long applicationId, PaymentStatus status) {
        return statisticRepository.getOrderCountByStatus(applicationId, status);
    }

    public StatisticSummary getStatisticsForApplication(Long applicationId) {
        StatisticSummary summary = new StatisticSummary();
        summary.setTotalMoneySuccess(getTotalMoneyByStatus(applicationId, PaymentStatus.SUCCESS));
        summary.setTotalOrdersSuccess(getOrderCountByStatus(applicationId, PaymentStatus.SUCCESS));
        summary.setTotalOrdersCancelled(getOrderCountByStatus(applicationId, PaymentStatus.CANCELLED));
        summary.setTotalOrdersPending(getOrderCountByStatus(applicationId, PaymentStatus.PENDING));
        summary.setTotalMoneyRefunded(getTotalMoneyByStatus(applicationId, PaymentStatus.REFUNDED)); // Thêm số tiền hoàn tiền
        summary.setTotalOrdersRefunded(getOrderCountByStatus(applicationId, PaymentStatus.REFUNDED));
        return summary;
    }

}

