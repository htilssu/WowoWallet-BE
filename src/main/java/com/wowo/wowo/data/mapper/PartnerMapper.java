package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.response.PartnerDto;
import com.wowo.wowo.data.dto.response.PartnerRequest;
import com.wowo.wowo.models.Partner;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;

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

    @AfterMapping
    default void create(@MappingTarget PartnerDto partnerDto, Partner partner) {
        if (partner.getCreated() != null) {
            partnerDto.setCreated(DateTimeFormatter.ISO_DATE_TIME.format(partner.getCreated().atStartOfDay()));
        }

    }
}