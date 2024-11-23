package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.CreateUserDTO;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateFromDTO(UserDTO userDTO, @MappingTarget User user);



    CreateUserDTO toDTORequest(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateFromCreateDTO(CreateUserDTO createUserDTO, @MappingTarget User user);
}
