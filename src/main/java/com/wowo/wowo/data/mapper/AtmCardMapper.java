package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.response.AtmCardDto;
import com.wowo.wowo.models.AtmCard;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface AtmCardMapper {

    AtmCard toEntity(AtmCardDto atmCardDto);

    AtmCardDto toDto(AtmCard atmCard);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AtmCard partialUpdate(
            AtmCardDto atmCardDto,
            @MappingTarget AtmCard atmCard);

    List<AtmCardDto> usersToUserDTOs(List<AtmCard> users);
}