package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.dto.response.UserDto;
import com.ewallet.ewallet.models.User;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    @AfterMapping
    default void create(@MappingTarget UserDto userDto, User user) {
        if (user.getCreated() != null) {
            userDto.setCreated(user.getCreated().toString());
        }
        if (user.getDob() != null) {
            userDto.setDob(user.getDob().toString());
        }
    }

    @Mappings(
            @Mapping(target = "dob", ignore = true)
    )
    User toEntity(com.ewallet.ewallet.dto.request.UserDto userDto);
    com.ewallet.ewallet.dto.request.UserDto toDtoRequest(User user);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(
            com.ewallet.ewallet.dto.request.UserDto userDto,
            @MappingTarget User user);

    @AfterMapping
    default void createFromRequestDto(@MappingTarget
    User user,
            com.ewallet.ewallet.dto.request.UserDto userDto) {
        if (userDto.getDob() != null) {
            try {
                user.setDob(
                        LocalDate.from(DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                .parse(userDto.getDob())));
            } catch (Exception e) {
                user.setDob(null);
            }
        }
    }
}