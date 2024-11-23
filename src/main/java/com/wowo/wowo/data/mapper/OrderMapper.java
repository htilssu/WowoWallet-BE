package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.OrderCreateDTO;
import com.wowo.wowo.data.dto.OrderDTO;
import com.wowo.wowo.model.Order;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order toEntity(OrderDTO orderDTO);
    OrderDTO toDto(Order order);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(
            OrderDTO orderDTO,
            @MappingTarget Order order);
    Order toEntity(OrderCreateDTO orderCreateDTO);
    OrderCreateDTO toCreateDTO(Order order);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(
            OrderCreateDTO orderCreateDTO,
            @MappingTarget Order order);
}