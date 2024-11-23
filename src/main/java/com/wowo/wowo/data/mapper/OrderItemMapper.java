package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.OrderItemCreateDTO;
import com.wowo.wowo.model.OrderItem;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemCreateDTO orderItemCreateDTO);
    OrderItemCreateDTO toDTO(OrderItem orderItem);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderItem partialUpdate(
            OrderItemCreateDTO orderItemCreateDTO,
            @MappingTarget OrderItem orderItem);
}