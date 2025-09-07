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
 *  * Created: 31-10-2024
 *  ******************************************************
 */

package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.AtmCardCreationDTO;
import com.wowo.wowo.data.dto.AtmCardDTO;
import com.wowo.wowo.data.mapper.AtmCardMapper;
import com.wowo.wowo.data.mapper.AtmCardMapperImpl;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.AtmCard;
import com.wowo.wowo.model.User;
import com.wowo.wowo.repository.AtmCardRepository;
import com.wowo.wowo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Data
public class AtmCardService {

    private final UserRepository userRepository;
    private final AtmCardMapperImpl atmCardMapperImpl;
    private final AtmCardRepository atmCardRepository;
    private final AtmCardMapper atmCardMapper;

    public AtmCardDTO createAtmCard(AtmCardCreationDTO atmCardCreationDTO,
            Authentication authentication) {
        var currentDate = LocalDate.now();
        var cardDate = LocalDate.of(2000 + atmCardCreationDTO.getYear(), atmCardCreationDTO.getMonth(),
                1);
        cardDate = cardDate.withDayOfMonth(cardDate.lengthOfMonth());
        if (cardDate.isBefore(currentDate)) {
            throw new IllegalArgumentException("Thẻ đã hết hạn");
        }

        if (currentDate.plusYears(5).isBefore(cardDate)) {
            throw new IllegalArgumentException("Hạn sử dụng thẻ không được quá 5 năm");
        }
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId).orElse(null);
        final AtmCard atmCard = atmCardMapperImpl.toEntity(atmCardCreationDTO);
        atmCard.setOwner(user);

        atmCardRepository.findByCardNumber(atmCard.getCardNumber())
                .ifPresent(existingCard -> {
                    throw new NotFoundException("Thẻ đã tồn tại");
                });

        return atmCardMapper.toDto(atmCardRepository.save(atmCard));

    }
}
