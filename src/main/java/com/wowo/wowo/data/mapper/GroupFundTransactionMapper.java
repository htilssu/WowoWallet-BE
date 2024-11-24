package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.GroupFundTransactionDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupFundTransactionMapper {

    GroupFundTransaction toEntity(GroupFundTransactionDTO groupFundTransactionDTO);
    GroupFundTransactionDTO toDto(GroupFundTransaction groupFundTransaction);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GroupFundTransaction partialUpdate(
            GroupFundTransactionDTO groupFundTransactionDTO,
            @MappingTarget GroupFundTransaction groupFundTransaction);
}