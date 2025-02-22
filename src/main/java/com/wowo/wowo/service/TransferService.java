package com.wowo.wowo.service;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.constant.Constant;
import com.wowo.wowo.data.dto.ApplicationTransferDTO;
import com.wowo.wowo.data.dto.TransferDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.UserWallet;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.ApplicationRepository;
import com.wowo.wowo.repository.ConstantRepository;
import com.wowo.wowo.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransferService {

    private final ConstantRepository constantRepository;
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public Transaction transferWithLimit(TransferDTO data, Authentication authentication) {
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
    public Transaction transfer(TransferDTO data, Authentication authentication) {

        Wallet senderWallet = walletRepository.findById(data.getSourceId())
                .orElseThrow(
                        () -> new NotFoundException("Không tìm thấy ví"));
        String senderName = "";
        if (senderWallet instanceof UserWallet userWallet) {
            if (!userWallet.getUser()
                    .getId()
                    .equals(authentication.getPrincipal()
                            .toString())) {

                throw new BadRequest("Không thể chuyển tiền từ ví này");
            }
            senderName = userWallet.getUser()
                    .getFullName();
        }

        var user = userService.getUserByIdOrUsernameOrEmail(data.getReceiverId(),
                data.getReceiverId(),
                data.getReceiverId());

        var receiveWallet = user.getWallet();


        Transaction transaction = transferMoney(senderWallet, receiveWallet,
                data.getMoney());

        transaction.setSenderName(senderName);
        transaction.setReceiverName(user.getFullName());

        transaction.setMessage(data.getMessage());

        return transaction;
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
     * @return {@link Transaction} chứa thông tin giao dịch
     *
     * @throws InsufficientBalanceException nếu số dư của ví nguồn nhỏ hơn số tiền chuyển
     * @see InsufficientBalanceException
     */
    @Transactional
    public Transaction transferMoney(Wallet source,
            Wallet destination,
            long amount) throws
                         InsufficientBalanceException {


        if (source.getId()
                .equals(destination.getId())) throw new BadRequest(
                "Không thể chuyển tiền từ ví này đến chính ví này");

        transferWithNoFee(source, destination, amount);

        final var transaction = Transaction.builder()
                .amount(amount)
                .receiveWallet(destination)
                .senderWallet(source)
                .message("Chuyển tiền")
                .flowType(FlowType.TRANSFER_MONEY)
                .created(Instant.now())
                .updated(Instant.now())
                .build();

        return transactionService.save(transaction);
    }

    public void transferWithNoFee(Wallet source, Wallet destination, long amount) {
        if (amount <= 0) {
            throw new BadRequest("Số tiền chuyển phải lớn hơn 0");
        }
        if (source.getBalance() < amount) {
            log.warn("Not enough money when transfer from {} to {} with amount {}",
                    source.getId(), destination.getId(), amount);
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

    public void transfer(ApplicationTransferDTO transferDTO, Authentication authentication) {
        var applicationId = Long.valueOf(authentication.getPrincipal()
                .toString());
        var application = applicationRepository.findById(applicationId)
                .orElseThrow();
        var sourceWallet = application.getWallet();
        var destinationWallet = walletRepository.findById(transferDTO.getWalletId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ví"));

        transferWithNoFee(sourceWallet, destinationWallet, transferDTO.getAmount());

        //TODO: save transaction
    }

    public void withdraw(ApplicationTransferDTO transferDTO, Authentication authentication) {
        var applicationId = Long.valueOf(authentication.getPrincipal()
                .toString());
        var application = applicationRepository.findById(applicationId)
                .orElseThrow();
        var sourceWallet = walletService.getWallet(transferDTO.getWalletId());
        var destinationWallet = application.getWallet();

        transferWithNoFee(sourceWallet, destinationWallet, transferDTO.getAmount());

        //TODO: save transaction
    }
}
