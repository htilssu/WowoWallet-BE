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

package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.AtmCardCreateDto;
import com.wowo.wowo.data.dto.AtmCardDto;
import com.wowo.wowo.data.mapper.AtmCardMapper;
import com.wowo.wowo.data.mapper.AtmCardMapperImpl;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.AtmCard;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.AtmCardRepository;
import com.wowo.wowo.repositories.UserRepository;
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

    public AtmCardDto createAtmCard(AtmCardCreateDto atmCardCreateDto,
            Authentication authentication) {
        var currentDate = LocalDate.now();
        var cardDate = LocalDate.of(2000 + atmCardCreateDto.getYear(), atmCardCreateDto.getMonth(),
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
        final AtmCard atmCard = atmCardMapperImpl.toEntity(atmCardCreateDto);
        atmCard.setOwner(user);

        atmCardRepository.findByCardNumber(atmCard.getCardNumber())
                .ifPresent(_ -> {
                    throw new NotFoundException("Thẻ đã tồn tại");
                });

        return atmCardMapper.toDto(atmCardRepository.save(atmCard));

    }
}
