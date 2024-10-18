package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.request.CreateUserDto;
import com.wowo.wowo.data.dto.response.UserDto;
import com.wowo.wowo.models.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateFromDto(UserDto userDto, @MappingTarget User user);

    CreateUserDto toDtoRequest(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateFromCreateDto(CreateUserDto createUserDto, @MappingTarget User user);
}
