package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.PartnerDto;
import com.wowo.wowo.data.dto.PartnerRequest;
import com.wowo.wowo.model.Partner;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartnerMapper {

    Partner toEntity(PartnerDto partnerDto);
    Partner toEntity(PartnerRequest partnerRequest);

    @Mapping(target = "created", ignore = true)
    PartnerDto toDto(Partner partner);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Partner partialUpdate(
            PartnerDto partnerDto,
            @MappingTarget Partner partner);

}