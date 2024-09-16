package com.wowo.wowo.mongo.documents;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface EquityMapper {

    Equity toEntity(EquityDto equityDto);
    EquityDto toDto(Equity equity);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Equity partialUpdate(
            EquityDto equityDto,
            @MappingTarget Equity equity);
}