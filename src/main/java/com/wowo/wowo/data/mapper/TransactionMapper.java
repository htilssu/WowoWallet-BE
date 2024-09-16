package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.request.TransactionRequest;
import com.wowo.wowo.data.dto.response.TransactionResponse;
import com.wowo.wowo.models.Transaction;
import org.mapstruct.*;

import java.util.List;

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

    @Mappings({
            @Mapping(target = "created", source = "created"),
            @Mapping(target = "updated", source = "updated")
    })
    TransactionResponse toResponse(Transaction transaction);

    List<TransactionResponse> toListDto(List<Transaction> transactionList);


}
