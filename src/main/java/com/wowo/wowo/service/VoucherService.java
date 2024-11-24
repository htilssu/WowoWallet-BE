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
 *  * Created: 13-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.service;

import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.Voucher;
import com.wowo.wowo.repository.VoucherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public Voucher getVoucherOrThrow(String voucherId) {
        return voucherRepository.findById(voucherId)
                .orElseThrow(() -> new NotFoundException("Voucher not found"));
    }

    public Optional<Voucher> getVoucher(String voucherId) {
        return voucherRepository.findById(voucherId);
    }

    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }
}
