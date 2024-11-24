package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.OrderItemCreationDTO;
import com.wowo.wowo.model.OrderItem;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemCreationDTO orderItemCreationDTO);
    OrderItemCreationDTO toDto(OrderItem orderItem);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderItem partialUpdate(
            OrderItemCreationDTO orderItemCreationDTO,
            @MappingTarget OrderItem orderItem);
}