package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.OrderItemCreateDto;
import com.wowo.wowo.models.OrderItem;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemCreateDto orderItemCreateDto);
    OrderItemCreateDto toDto(OrderItem orderItem);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderItem partialUpdate(
            OrderItemCreateDto orderItemCreateDto,
            @MappingTarget OrderItem orderItem);
}