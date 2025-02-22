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

package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsApplication;
import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.*;
import com.wowo.wowo.data.mapper.ApplicationMapper;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.model.ApplicationPartnerWallet;
import com.wowo.wowo.repository.ApplicationPartnerWalletRepository;
import com.wowo.wowo.service.ApplicationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/application")
@IsUser
@Slf4j
@AllArgsConstructor
@Tag(name = "Application", description = "APIs for managing applications")
public class ApplicationController {

    private final WalletMapper walletMapper;
    private final ApplicationServiceImpl applicationServiceImpl;
    private final ApplicationMapper applicationMapper;
    private final ApplicationPartnerWalletRepository applicationPartnerWalletRepository;

    @PostMapping
    public ApplicationDTO createApplication(@RequestBody @Validated ApplicationUserCreationDTO applicationUserCreationDTO,
            Authentication authentication) {
        log.info("creating application with name {} for user {}",
                applicationUserCreationDTO.getName(),
                authentication.getPrincipal()
                        .toString());

        applicationUserCreationDTO.setOwnerId(authentication.getPrincipal()
                .toString());

        return applicationMapper.toDto(
                applicationServiceImpl.createApplication(applicationUserCreationDTO));
    }

    @GetMapping("/{id}")
    public ApplicationDTO getApplication(@PathVariable Long id, Authentication authentication) {
        log.info("user with id {} requested application with id {}", authentication.getPrincipal()
                .toString(), id);
        return applicationMapper.toDto(applicationServiceImpl.getApplicationOrElseThrow(
                id));
    }

    @PostMapping("/wallet")
    @IsApplication
    public WalletDTO createWallet(Authentication authentication) {
        log.info("creating wallet for application with id {}", authentication.getPrincipal()
                .toString());

        final ApplicationPartnerWallet wallet = applicationServiceImpl.createWallet(
                Long.valueOf(authentication.getPrincipal()
                        .toString()));
        return walletMapper.toDto(wallet);
    }

    @GetMapping("/wallet")
    @IsApplication
    public Collection<WalletDTO> getWallet(Authentication authentication, PagingDTO pagingDTO) {
        final Long applicationId = Long.valueOf(authentication.getPrincipal()
                .toString());
        log.info("getting wallet for application with id {}", applicationId);

        final List<ApplicationPartnerWallet> wallet =
                applicationPartnerWalletRepository.findByApplication_IdOrderByIdAsc(applicationId,
                        Pageable.ofSize(
                                        pagingDTO.getOffset())
                                .withPage(pagingDTO.getPage()));

        return wallet.stream()
                .map(walletMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/wallet")
    public @NotNull Collection<WalletDTO> getWallet(@PathVariable Long id, PagingDTO pagingDTO) {
        log.info("getting wallet for application with id {}", id);

        final List<ApplicationPartnerWallet> wallet =
                applicationPartnerWalletRepository.findByApplication_IdOrderByIdAsc(id,
                        Pageable.ofSize(
                                        pagingDTO.getOffset())
                                .withPage(pagingDTO.getPage()));

        return wallet.stream()
                .map(walletMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/wallet/{id}")
    @IsApplication
    public void deleteWallet(@PathVariable String id, Authentication authentication) {
        log.info("deleting wallet with id {}", id);

        applicationServiceImpl.deleteWallet(authentication, id);
    }

    @GetMapping("/wallet/{id}")
    @IsApplication
    public WalletDTO getWallet(@PathVariable String id, Authentication authentication) {
        log.info("getting wallet with id {}", id);
        return walletMapper.toDto(
                applicationServiceImpl.getWallet(authentication, Long.valueOf(id)));
    }

    @GetMapping("/{id}/order")
    public OrderHistoryDTO getListOrder(@PathVariable String id, PagingDTO pagingDTO) {
        return applicationServiceImpl.getApplicationOrder(Long.valueOf(id), pagingDTO);
    }

    @GetMapping("order")
    public OrderHistoryDTO getListOrder(Authentication authentication, PagingDTO pagingDTO) {
        final Long id = Long.valueOf(authentication.getPrincipal()
                .toString());
        return applicationServiceImpl.getApplicationOrder(id, pagingDTO);
    }
}
