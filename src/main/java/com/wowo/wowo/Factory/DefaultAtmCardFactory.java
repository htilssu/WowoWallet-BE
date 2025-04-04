package com.wowo.wowo.Factory;

import com.wowo.wowo.data.dto.AtmCardCreationDTO;
import com.wowo.wowo.data.mapper.AtmCardMapperImpl;
import com.wowo.wowo.model.AtmCard;
import com.wowo.wowo.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class DefaultAtmCardFactory extends AtmCardFactory {
    private final AtmCardMapperImpl atmCardMapperImpl;

    @Override
    public AtmCard createCard(AtmCardCreationDTO dto, User owner) {
        // Kiểm tra ngày hết hạn
        LocalDate currentDate = LocalDate.now();
        LocalDate cardDate = LocalDate.of(2000 + dto.getYear(), dto.getMonth(), 1);
        cardDate = cardDate.withDayOfMonth(cardDate.lengthOfMonth());

        if (cardDate.isBefore(currentDate)) {
            throw new IllegalArgumentException("Thẻ đã hết hạn");
        }
        if (currentDate.plusYears(5).isBefore(cardDate)) {
            throw new IllegalArgumentException("Hạn sử dụng thẻ không được quá 5 năm");
        }

        // Tạo đối tượng AtmCard
        AtmCard atmCard = atmCardMapperImpl.toEntity(dto);
        atmCard.setOwner(owner);
        return atmCard;
    }
}
