package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.request.CreateOrderDto;
import com.wowo.wowo.data.dto.response.OrderDto;
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
    Order toEntity(CreateOrderDto createOrderDto);
    CreateOrderDto toCreateDto(Order order);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(
            CreateOrderDto createOrderDto,
            @MappingTarget Order order);
}