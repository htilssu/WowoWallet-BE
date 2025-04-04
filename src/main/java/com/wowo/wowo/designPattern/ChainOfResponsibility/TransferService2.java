package com.wowo.wowo.designPattern.ChainOfResponsibility;

import com.wowo.wowo.data.dto.TransferDTO;
import com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler.BalanceCheckHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler.TransactionLoggingHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler.TransferHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.ConcreteHandler.WalletValidationHandler;
import com.wowo.wowo.designPattern.ChainOfResponsibility.Handle.TransferRequest;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.WalletRepository;
import com.wowo.wowo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService2 {

    private final BalanceCheckHandler balanceCheckHandler;
    private final UserService userService;
    private final WalletRepository walletRepository;

    @Autowired
    public TransferService2(BalanceCheckHandler balanceCheckHandler,
                            WalletValidationHandler walletValidationHandler,
                            TransferHandler transferHandler,
                            TransactionLoggingHandler transactionLoggingHandler, UserService userService, WalletRepository walletRepository) {
        this.balanceCheckHandler = balanceCheckHandler;
        this.userService = userService;
        this.walletRepository = walletRepository;

        // Thiết lập chuỗi: BalanceCheck -> WalletValidation -> Transfer -> TransactionLogging
        balanceCheckHandler.setNext(walletValidationHandler);
        walletValidationHandler.setNext(transferHandler);
        transferHandler.setNext(transactionLoggingHandler);
    }

    public Transaction processTransaction(TransferDTO transferDTO) {
        // Tìm ví nguồn và đích
        Wallet source = walletRepository.findById(transferDTO.getSourceId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ví nguồn"));
        var user = userService.getUserByIdOrUsernameOrEmail(transferDTO.getReceiverId(),
                transferDTO.getReceiverId(),
                transferDTO.getReceiverId());

        var receiveWallet = user.getWallet();

        // Tạo TransferRequest
        TransferRequest request = new TransferRequest(
                source,
                receiveWallet,
                transferDTO.getMoney(),
                transferDTO.getMessage()
        );
        return balanceCheckHandler.handle(request); // Bắt đầu chuỗi xử lý
    }
}