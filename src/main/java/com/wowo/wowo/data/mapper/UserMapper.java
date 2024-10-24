package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.CreateUserDto;
import com.wowo.wowo.data.dto.UserDto;
import com.wowo.wowo.models.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING, uses = {PartnerMapper.class})
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(
            UserDto userDto,
            @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateFromDto(UserDto userDto, @MappingTarget User user);

    CreateUserDto toDtoRequest(User user);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(
            CreateUserDto createUserDto,
            @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateFromCreateDto(CreateUserDto createUserDto, @MappingTarget User user);
}
