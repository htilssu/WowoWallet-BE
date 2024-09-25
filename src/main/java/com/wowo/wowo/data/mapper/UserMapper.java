package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.request.CreateUserDto;
import com.wowo.wowo.data.dto.response.UserDto;
import com.wowo.wowo.models.User;
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
            {
                    @Mapping(target = "dob", ignore = true),
                    @Mapping(target = "userName", source = "username")
            })
    User toEntity(CreateUserDto createUserDto);
    CreateUserDto toDtoRequest(User user);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(
            CreateUserDto createUserDto,
            @MappingTarget User user);

    @AfterMapping
    default void createFromRequestDto(@MappingTarget
    User user,
            CreateUserDto createUserDto) {
        if (createUserDto.getDob() != null) {
            try {
                user.setDob(LocalDate.parse(createUserDto.getDob()));
            } catch (Exception e) {
                try {
                    user.setDob(
                            LocalDate.from(DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    .parse(createUserDto.getDob())));
                } catch (Exception e1) {
                    user.setDob(
                            LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                    .parse(createUserDto.getDob())));
                }
            }
        }
    }
}