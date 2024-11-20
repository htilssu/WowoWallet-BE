package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.TransactionDto;
import com.wowo.wowo.data.dto.TransactionRequest;
import com.wowo.wowo.model.Transaction;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    @Mappings({
            @Mapping(target =
                             "id", ignore = true),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "updated", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Transaction toEntity(TransactionRequest transactionRequest);

    Transaction toEntity(TransactionDto transactionDto);
    TransactionDto toDto(Transaction transaction);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction partialUpdate(
            TransactionDto transactionDto,
            @MappingTarget Transaction transaction);
}
