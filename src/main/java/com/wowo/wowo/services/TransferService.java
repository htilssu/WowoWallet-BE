package com.wowo.wowo.services;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.WalletRepository;
import com.wowo.wowo.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    private final WalletRepository walletRepository;
    private final UserService userService;
    private final WalletTransactionService walletTransactionService;

    @IsUser
    @Transactional
    public WalletTransaction transfer(TransferDto data) {

        Wallet senderWallet;
        var authid = AuthUtil.getId();
        if (data.getSourceId() == null) {
            if (!authid.equals(data.getSenderId())) {
                throw new BadRequest("Không thể chuyển tiền từ ví không phải của bạn");
            }
            senderWallet = walletRepository.findByOwnerId(data.getSenderId())
                    .orElseThrow(
                            () -> new NotFoundException("Không tìm thấy ví"));
        }
        else {
            senderWallet = walletRepository.findById(Long.valueOf(data.getSourceId()))
                    .orElseThrow(
                            () -> new NotFoundException("Không tìm thấy ví"));
            var isOwner = authid.equals(senderWallet.getOwnerId());
            if (!isOwner) {
                throw new BadRequest("Không thể chuyển tiền từ ví không phải của bạn");
            }
        }
        final User receiver = userService.getUserByIdOrUsernameOrEmail(
                data.getReceiverId(), data.getReceiverId(),
                data.getReceiverId());
        var receiverWallet = walletRepository.findByOwnerId(receiver.getId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví người nhận"));

        assert senderWallet != null;

        WalletTransaction walletTransaction = transferMoney(senderWallet, receiverWallet,
                data.getMoney());

        walletTransaction =
                walletTransactionService.createWalletTransaction(walletTransaction);

        return walletTransaction;
    }

    public WalletTransaction transferMoney(Wallet source, Wallet destination, long amount) {
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

        return walletTransaction;
    }
}
