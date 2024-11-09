package com.wowo.wowo.services;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.repositories.WalletRepository;
import com.wowo.wowo.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    private final WalletRepository walletRepository;
    private final UserService userService;
    private final WalletTransactionService walletTransactionService;
    private final TransactionRepository transactionRepository;
    private final ReceiverService receiverService;

    @IsUser
    @Transactional
    public WalletTransaction transfer(TransferDto data, Authentication authentication) {

        var senderId = ((String) authentication.getPrincipal());

        Wallet senderWallet;
        var authid = AuthUtil.getId();
        if (data.getSourceId() == null) {
            if (!authid.equals(senderId)) {
                throw new BadRequest("Không thể chuyển tiền từ ví không phải của bạn");
            }
            senderWallet = walletRepository.findByOwnerId(senderId)
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
        final User user = userService.getUserById(senderWallet.getOwnerId());
        var receiverWallet = walletRepository.findByOwnerId(receiver.getId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví người nhận"));


        WalletTransaction walletTransaction = transferMoney(senderWallet, receiverWallet,
                data.getMoney());

        walletTransaction.getTransaction().setReceiverName(receiver.getFullName());
        walletTransaction.getTransaction().setSenderName(user.getFullName());

        walletTransaction.getTransaction().setMessage(data.getMessage());

        walletTransaction =
                walletTransactionService.createWalletTransaction(walletTransaction);

        return walletTransaction;
    }

    /**
     * Chuyển tiền từ ví nguồn tới ví đích
     * số tiền chuyển phải nhỏ hơn hoặc bằng số dư của ví nguồn, nếu không sẽ ném ra ngoại lệ
     * {@code InsufficientBalanceException}
     *
     * @param source      ví nguồn
     * @param destination ví đích
     * @param amount      số tiền chuyển
     *
     * @return {@link WalletTransaction} chứa thông tin giao dịch
     *
     * @throws InsufficientBalanceException nếu số dư của ví nguồn nhỏ hơn số tiền chuyển
     * @see InsufficientBalanceException
     */
    @Transactional
    public WalletTransaction transferMoney(Wallet source, Wallet destination, long amount) throws
                                                                                           InsufficientBalanceException {


        if (source.getId().equals(destination.getId())) throw new BadRequest(
                "Không thể chuyển tiền từ ví này đến chính ví này");
        transfer(source, destination, amount);

        walletRepository.saveAll(List.of(source, destination));


        final var walletTransaction = new WalletTransaction();
        walletTransaction.setSenderWallet(source);
        walletTransaction.setReceiverWallet(destination);
        walletTransaction.setType(TransactionType.TRANSFER);

        Transaction transaction = new Transaction();

        transaction.setVariant(TransactionVariant.WALLET);
        transaction.setAmount(amount);
        transaction.setStatus(PaymentStatus.SUCCESS);
        transaction.setType(FlowType.OUT);
        transaction.setMessage("Chuyển tiền");

        walletTransaction.setTransaction(transaction);

        return walletTransactionService.createWalletTransaction(walletTransaction);
    }

    public void transfer(BalanceEntity source, BalanceEntity destination, long amount) {
        if (source.getBalance() < amount) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
    }
}
