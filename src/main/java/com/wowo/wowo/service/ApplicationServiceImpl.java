/*
 * ******************************************************
 *  * Copyright (c) 2024 htilssu
 *  *
 *  * This code is the property of htilssu. All rights reserved.
 *  * Redistribution or reproduction of any part of this code
 *  * in any form, with or without modification, is strictly
 *  * prohibited without prior written permission from the author.
 *  *
 *  * Author: htilssu
 *  * Created: 23-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.service;

import com.wowo.wowo.constant.Constant;
import com.wowo.wowo.data.dto.ApplicationUserCreationDTO;
import com.wowo.wowo.data.dto.OrderHistoryDTO;
import com.wowo.wowo.data.dto.PagingDTO;
import com.wowo.wowo.data.mapper.ApplicationMapper;
import com.wowo.wowo.data.mapper.OrderMapper;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.ApplicationRepository;
import com.wowo.wowo.repository.OrderRepository;
import com.wowo.wowo.repository.TransactionRepository;
import com.wowo.wowo.util.ApiKeyUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final OrderMapper orderMapper;

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserService userService;
    private final WalletService walletService;
    private final OrderRepository orderRepository;
    private final TransferService transferService;
    private final TransactionRepository transactionRepository;
    private final ConstantService constantService;

    @Override
    public Application createApplication(ApplicationUserCreationDTO applicationUserCreationDTO) {
        final Application application = applicationMapper.toEntity(applicationUserCreationDTO);
        final ApplicationWallet applicationWallet = new ApplicationWallet();
        applicationWallet.setApplication(application);

        var user = userService.getUserByIdOrElseThrow(applicationUserCreationDTO.getOwnerId());
        application.setOwner(user);
        application.setSecret(ApiKeyUtil.generateApiKey());
        application.setWallet(applicationWallet);

        return applicationRepository.save(application);
    }

    @Override
    public void deleteApplication(String applicationId) {

    }

    @Override
    public Optional<Application> getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    @Override
    public Application getApplicationByUserId(String userId) {
        return null;
    }

    @Override
    public Application getApplicationOrElseThrow(Long applicationId) {

        return getApplication(applicationId).orElseThrow(
                () -> {
                    log.error("Application with id {} not found", applicationId);
                    return new NotFoundException("Application not found");
                });
    }

    @Override
    public ApplicationPartnerWallet createWallet(Long applicationId) {
        final Application application = getApplicationOrElseThrow(applicationId);
        final ApplicationPartnerWallet applicationPartnerWallet = new ApplicationPartnerWallet();
        applicationPartnerWallet.setApplication(application);
        application.getPartnerWallets()
                .add(applicationPartnerWallet);

        final Application app = save(application);
        var partnerWalletSize = app.getPartnerWallets()
                .size();
        return app.getPartnerWallets()
                .stream()
                .toList()
                .get(partnerWalletSize - 1);
    }

    private Application save(Application application) {
        try {
            return applicationRepository.save(application);
        } catch (Exception e) {
            log.error("Error saving application {}: {}", application.toString(), e.getMessage());
        }
        return application;
    }

    @Override
    public Optional<Application> getApplicationBySecretKey(String apiKey) {
        return applicationRepository.findFirstBySecret(apiKey);
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Transaction withdraw(String id, Long amount) {
        Application application = getApplicationOrElseThrow(Long.valueOf(id));
        final ApplicationWallet wallet = application.getWallet();
        var userWallet = application.getOwner()
                .getWallet();

        final com.wowo.wowo.model.Constant minWithdraw = constantService.findByKey(
                Constant.MINIMUM_WITHDRAW_APPLICATION);
        if (amount < minWithdraw.getValue()) {
            throw new BadRequest("Số tiền rút phải lớn hơn " + minWithdraw.getValue());
        }

        transferService.transferWithNoFee(wallet, userWallet, amount);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setFlowType(FlowType.WITHDRAW_APPLICATION);
        transaction.setMessage("Rút tiền từ application");
        transaction.setReceiverName(application.getOwner()
                .getFullName());
        transaction.setSenderName(application.getName());
        transaction.setSenderWallet(wallet);
        transaction.setReceiveWallet(userWallet);

        return transactionRepository.save(transaction);
    }

    public void deleteWallet(Authentication authentication, String id) {
        var applicationId = Long.valueOf(authentication.getPrincipal()
                .toString());
        var application = getApplicationOrElseThrow(applicationId);
        application.getPartnerWallets()
                .removeIf(wallet -> wallet.getId()
                        .equals(Long.valueOf(id)));
        walletService.deleteWallet(Long.valueOf(id));
        applicationRepository.save(application);
    }

    public Wallet getWallet(Authentication authentication, Long aLong) {
        var applicationId = Long.valueOf(authentication.getPrincipal()
                .toString());
        var application = getApplicationOrElseThrow(applicationId);
        return application.getPartnerWallets()
                .stream()
                .filter(wallet -> wallet.getId()
                        .equals(aLong))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Wallet not found"));
    }

    public OrderHistoryDTO getApplicationOrder(Long applicationId, PagingDTO pagingDTO) {
        final List<Order> list = orderRepository.findByApplication_IdOrderByUpdatedDesc(
                        applicationId,
                        Pageable.ofSize(pagingDTO.getOffset())
                                .withPage(pagingDTO.getPage()))
                .stream()
                .toList();

        final long total = orderRepository.countByApplication_Id(applicationId);

        return new OrderHistoryDTO(list.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList()), total);
    }
}
