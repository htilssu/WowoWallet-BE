package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.CreateOrderData;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.models.Order;
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
    Order toEntity(CreateOrderData createOrderDto);
    CreateOrderData toCreateDto(Order order);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(
            CreateOrderData createOrderDto,
            @MappingTarget Order order);
}