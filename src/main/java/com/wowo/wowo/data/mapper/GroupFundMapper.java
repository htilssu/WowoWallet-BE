package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.GroupFundDto;
import com.wowo.wowo.data.dto.GroupFundTransactionDto;
import com.wowo.wowo.model.GroupFund;
import com.wowo.wowo.model.GroupFundTransaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupFundMapper {
    GroupFundTransactionDto toTransactionDto(GroupFundTransaction transaction);

    GroupFund toEntity(GroupFundDto groupFundDto);
    GroupFundDto toDto(GroupFund groupFund);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)GroupFund partialUpdate(
            GroupFundDto groupFundDto,
            @MappingTarget GroupFund groupFund);
}