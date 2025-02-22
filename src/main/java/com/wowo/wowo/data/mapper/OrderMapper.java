package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.OrderCreationDTO;
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
    Order toEntity(OrderCreationDTO orderCreationDTO);
    OrderCreationDTO toCreateDTO(Order order);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(
            OrderCreationDTO orderCreationDTO,
            @MappingTarget Order order);
}