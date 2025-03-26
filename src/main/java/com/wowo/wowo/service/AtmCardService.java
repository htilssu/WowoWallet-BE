package com.wowo.wowo.service;

import com.wowo.wowo.Factory.AtmCardFactory;
import com.wowo.wowo.data.dto.AtmCardCreationDTO;
import com.wowo.wowo.data.dto.AtmCardDTO;
import com.wowo.wowo.data.mapper.AtmCardMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.AtmCard;
import com.wowo.wowo.model.User;
import com.wowo.wowo.repository.AtmCardRepository;
import com.wowo.wowo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class AtmCardService {
    private final UserRepository userRepository;
    private final AtmCardRepository atmCardRepository;
    private final AtmCardMapper atmCardMapper;
    private final AtmCardFactory atmCardFactory;

    public AtmCardDTO createAtmCard(AtmCardCreationDTO atmCardCreationDTO, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));

        AtmCard atmCard = atmCardFactory.createCard(atmCardCreationDTO, user);

        atmCardRepository.findByCardNumber(atmCard.getCardNumber())
                .ifPresent(_ -> { throw new NotFoundException("Thẻ đã tồn tại"); });

        return atmCardMapper.toDto(atmCardRepository.save(atmCard));
    }
}
