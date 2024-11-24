package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.GroupFundDTO;
import com.wowo.wowo.data.dto.GroupFundTransactionDTO;
import com.wowo.wowo.model.GroupFund;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupFundMapper {
    GroupFundTransactionDTO toTransactionDTO(GroupFundTransaction transaction);

    GroupFund toEntity(GroupFundDTO groupFundDTO);
    GroupFundDTO toDto(GroupFund groupFund);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)GroupFund partialUpdate(
            GroupFundDTO groupFundDTO,
            @MappingTarget GroupFund groupFund);
}