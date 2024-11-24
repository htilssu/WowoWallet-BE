package com.wowo.wowo.service;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.constant.Constant;
import com.wowo.wowo.data.dto.TransferDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.ConstantRepository;
import com.wowo.wowo.repository.UserWalletRepository;
import com.wowo.wowo.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    private final UserWalletRepository userWalletRepository;
    private final UserService userService;
    private final WalletTransactionService walletTransactionService;
    private final ConstantRepository constantRepository;
    private final WalletRepository walletRepository;
    private final WalletService walletService;

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

        Wallet senderWallet = walletRepository.findById(data.getSourceId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví"));

        //TODO: check owner of wallet

        final User user = userService.getUserByIdOrElseThrow(senderWallet.getOwnerId());
        var receiveWallet = walletRepository.findById(data.getSourceId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví người nhận"));


        WalletTransaction walletTransaction = transferMoney(senderWallet, receiveWallet,
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
    public WalletTransaction transferMoney(Wallet source,
            Wallet destination,
            long amount) throws
                         InsufficientBalanceException {


        if (source.getId()
                .equals(destination.getId())) throw new BadRequest(
                "Không thể chuyển tiền từ ví này đến chính ví này");

        transferWithNoFee(source, destination, amount);

        final var walletTransaction = new WalletTransaction();
        walletTransaction.setSenderUserWallet(source);
        walletTransaction.setReceiverUserWallet(destination);
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

    public void transferWithNoFee(Wallet source, Wallet destination, long amount) {
        if (source.getBalance() < amount) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
        walletRepository.saveAll(List.of(source, destination));
    }

    public void transferWithFee(Wallet source, Wallet destination, long amount, long fee) {
        transfer(source, destination, amount);
        transferToRoot(source, fee);
        walletService.save(source, destination);
    }

    public void transfer(Wallet source, Wallet destination, long amount) {
        if (source.getBalance() < amount) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        source.setBalance(source.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);
    }

    public void transferToRoot(Wallet source, long amount) {
        final var rootWallet = walletService.getRootWallet();
        transfer(source, rootWallet, amount);
        walletService.save(rootWallet);
    }

    public void transferWithTax(Wallet source, Wallet destination, long amount, long tax) {
        tax = (amount * tax) / 100;
        amount = amount - tax;
        transfer(source, destination, amount);
        transferToRoot(source, tax);
        walletService.save(source, destination);
    }
}
