package com.wowo.wowo.services;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    private final WalletRepository walletRepository;
    private final UserService userService;
    private final TransactionService transactionService;

    @IsUser
    @Transactional
    public ResponseEntity<?> transfer(TransferDto data) {

        Wallet senderWallet = null;
        if (data.getSourceId() == null) {
            senderWallet = walletRepository.findByOwnerId(data.getSenderId())
                    .orElseThrow(
                            () -> new NotFoundException("Không tìm thấy ví"));
        }
        final User receiver = userService.getUserByIdOrUsernameOrEmail(
                data.getReceiverId(), data.getReceiverId(),
                data.getReceiverId());
        var receiverWallet = walletRepository.findByOwnerId(receiver.getId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví người nhận"));

        assert senderWallet != null;

        transferMoney(senderWallet, receiverWallet, data.getMoney());

        return ResponseEntity.ok().build();
    }

    protected void transferMoney(Wallet source, Wallet destination, long amount) {
        if (source.getBalance() < amount) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        walletRepository.saveAll(List.of(source, destination));


        final var walletTransaction = new WalletTransaction();
        walletTransaction.setSenderWallet(source);
        walletTransaction.setReceiverWallet(destination);

        Transaction transaction = new Transaction();

        transaction.setVariant(TransactionVariant.WALLET);
        transaction.setAmount(amount);
        transaction.setStatus(PaymentStatus.SUCCESS);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setDescription("Chuyển tiền");

        walletTransaction.setTransaction(transaction);
    }
}
