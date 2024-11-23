package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.PartnerDTO;
import com.wowo.wowo.data.dto.PartnerRequest;
import com.wowo.wowo.model.Partner;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartnerMapper {

    Partner toEntity(PartnerDTO partnerDTO);
    Partner toEntity(PartnerRequest partnerRequest);

    @Mapping(target = "created", ignore = true)
    PartnerDTO toDto(Partner partner);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Partner partialUpdate(
            PartnerDTO partnerDTO,
            @MappingTarget Partner partner);

}