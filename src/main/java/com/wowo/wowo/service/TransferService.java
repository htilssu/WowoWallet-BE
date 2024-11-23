package com.wowo.wowo.service;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.constant.Constant;
import com.wowo.wowo.data.dto.TransferDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.ConstantRepository;
import com.wowo.wowo.repository.TransactionRepository;
import com.wowo.wowo.repository.WalletRepository;
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
    private final ConstantRepository constantRepository;

    @Transactional
    public WalletTransaction transferWithLimit(TransferDTO data, Authentication authentication) {
        var minTransfer = constantRepository.findById(Constant.MINIMUM_TRANSFER_AMOUNT)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cài đặt"));

        var maxTransfer = constantRepository.findById(Constant.MAXIMUM_TRANSFER_AMOUNT)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cài đặt"));

        if (data.getMoney() < minTransfer.getValue()) {
            throw new BadRequest("Số tiền chuyển phải lớn hơn hoặc bằng" + minTransfer.getValue());
        }
        if (data.getMoney() > maxTransfer.getValue()) {
            throw new BadRequest("Số tiền chuyển phải nhỏ hơn hoặc bằng" + maxTransfer.getValue());
        }


        return transfer(data, authentication);
    }

    @IsUser
    @Transactional
    public WalletTransaction transfer(TransferDTO data, Authentication authentication) {

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
        final User user = userService.getUserByIdOrElseThrow(senderWallet.getOwnerId());
        var receiverWallet = walletRepository.findByOwnerId(receiver.getId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví người nhận"));


        WalletTransaction walletTransaction = transferMoney(senderWallet, receiverWallet,
                data.getMoney());

        walletTransaction.getTransaction()
                .setReceiverName(receiver.getFullName());
        walletTransaction.getTransaction()
                .setSenderName(user.getFullName());

        walletTransaction.getTransaction()
                .setMessage(data.getMessage());

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


        if (source.getId()
                .equals(destination.getId())) throw new BadRequest(
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
