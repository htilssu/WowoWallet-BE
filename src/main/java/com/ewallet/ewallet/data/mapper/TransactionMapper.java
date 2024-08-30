package com.ewallet.ewallet.data.mapper;

import com.ewallet.ewallet.data.dto.request.TransactionRequest;
import com.ewallet.ewallet.data.dto.response.TransactionResponse;
import com.ewallet.ewallet.models.Transaction;
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
