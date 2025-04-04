package com.wowo.wowo.decorator;

import com.wowo.wowo.data.dto.WithdrawDTO;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.TransactionRepository;
import com.wowo.wowo.service.TransferService;
import com.wowo.wowo.service.WalletService;
import org.springframework.stereotype.Service;

@Service
public class BasicWithdrawService implements WithdrawService {
    private final WalletService walletService;
    private final TransferService transferService;
    private final TransactionRepository transactionRepository;
    private WithdrawDTO withdrawDTO;

    public BasicWithdrawService(WalletService walletService, TransferService transferService,
            TransactionRepository transactionRepository) {
        this.walletService = walletService;
        this.transferService = transferService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void withdraw(WithdrawDTO withdrawDTO) {
        this.withdrawDTO = withdrawDTO;
        final Wallet wallet = walletService.getWallet(Long.valueOf(withdrawDTO.getSourceId()));
        final Wallet rootWallet = walletService.getRootWallet();
        transferService.transferWithNoFee(wallet, rootWallet, withdrawDTO.getAmount());

        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawDTO.getAmount());
        transaction.setSenderWallet(wallet);
        transaction.setReceiveWallet(rootWallet);
        transaction.setFlowType(FlowType.WITHDRAW);
        transaction.setReceiverName("Ngân hàng liên kết");
        transactionRepository.save(transaction);
    }
}