package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.OrderCreateDto;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.model.Order;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order toEntity(OrderDto orderDto);
    OrderDto toDto(Order order);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(
            OrderDto orderDto,
            @MappingTarget Order order);
    Order toEntity(OrderCreateDto orderCreateDto);
    OrderCreateDto toCreateDto(Order order);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(
            OrderCreateDto orderCreateDto,
            @MappingTarget Order order);
}