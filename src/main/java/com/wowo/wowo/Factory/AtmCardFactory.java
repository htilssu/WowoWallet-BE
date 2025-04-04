package com.wowo.wowo.Factory;

import com.wowo.wowo.data.dto.AtmCardCreationDTO;
import com.wowo.wowo.model.AtmCard;
import com.wowo.wowo.model.User;

public abstract class AtmCardFactory {
    // Phương thức Factory Method
    public abstract AtmCard createCard(AtmCardCreationDTO dto, User owner);
}