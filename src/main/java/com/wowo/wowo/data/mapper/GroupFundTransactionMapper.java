package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.GroupFundTransactionDto;
import com.wowo.wowo.model.GroupFundTransaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupFundTransactionMapper {

    GroupFundTransaction toEntity(GroupFundTransactionDto groupFundTransactionDto);
    GroupFundTransactionDto toDto(GroupFundTransaction groupFundTransaction);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GroupFundTransaction partialUpdate(
            GroupFundTransactionDto groupFundTransactionDto,
            @MappingTarget GroupFundTransaction groupFundTransaction);
}