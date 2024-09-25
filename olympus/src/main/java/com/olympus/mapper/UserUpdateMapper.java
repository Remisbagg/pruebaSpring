package com.olympus.mapper;

import com.olympus.dto.request.UserUpdate;
import com.olympus.entity.Gender;
import com.olympus.entity.MaritalStatus;
import com.olympus.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserUpdateMapper {
    @Mapping(source = "dto.firstName", target = "entity.firstName")
    @Mapping(source = "dto.lastName", target = "entity.lastName")
    @Mapping(source = "dto.birthDate", target = "entity.birthDate")
    @Mapping(source = "dto.phoneNumber", target = "entity.phoneNumber")
    @Mapping(source = "dto.currentAddress", target = "entity.currentAddress")
    @Mapping(source = "dto.occupation", target = "entity.occupation")
    @Mapping(target = "entity.gender", ignore = true)
    @Mapping(target = "entity.status", ignore = true)
    void updateEntityFromDTO(UserUpdate dto, @MappingTarget User entity);

    @AfterMapping
    default void afterMappingUserUpdateDTOToUser(@MappingTarget User entity, UserUpdate dto) {
        if (dto.getGender() != null) {
            entity.setGender(Gender.valueOf(dto.getGender().trim().toUpperCase()));
        }

        if (dto.getStatus() != null) {
            entity.setStatus(MaritalStatus.valueOf(dto.getStatus().trim().toUpperCase()));
        }
    }
}
