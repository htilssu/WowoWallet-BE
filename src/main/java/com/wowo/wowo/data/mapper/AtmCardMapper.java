package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.AtmCardCreateDTO;
import com.wowo.wowo.data.dto.AtmCardDTO;
import com.wowo.wowo.model.AtmCard;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface AtmCardMapper {

    AtmCard toEntity(AtmCardDTO atmCardDTO);

    AtmCardDTO toDto(AtmCard atmCard);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AtmCard partialUpdate(
            AtmCardDTO atmCardDTO,
            @MappingTarget AtmCard atmCard);

    List<AtmCardDTO> usersToUserDTOs(List<AtmCard> users);
    AtmCard toEntity(AtmCardCreateDTO atmCardCreateDTO);
}