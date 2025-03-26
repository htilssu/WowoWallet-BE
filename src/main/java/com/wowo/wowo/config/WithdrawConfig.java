package com.wowo.wowo.config;

import com.wowo.wowo.Decorator.*;
import com.wowo.wowo.repository.TransactionRepository;
import com.wowo.wowo.service.TransferService;
import com.wowo.wowo.service.WalletService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WithdrawConfig {

    @Bean
    public WithdrawService withdrawService(WalletService walletService, TransferService transferService,
                                           TransactionRepository transactionRepository) {
        WithdrawService basicService = new BasicWithdrawService(walletService, transferService, transactionRepository);
        WithdrawService balanceChecked = new BalanceCheckWithdrawDecorator(basicService, walletService);
        WithdrawService logged = new LoggingWithdrawDecorator(balanceChecked);
        return new FeeWithdrawDecorator(logged, transferService, 5000L); // Phí 5,000
    }
}