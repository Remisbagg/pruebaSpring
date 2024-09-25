package com.back.mapper;

import com.back.dto.response.friendship.FriendDTO;
import com.back.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FriendMapper {
    @Mapping(source = "entity.id", target = "userId")
    @Mapping(source = "entity.avatar", target = "avatar")
    @Mapping(source = "entity.firstName", target = "firstName")
    @Mapping(source = "entity.lastName", target = "lastName")
    @Mapping(source = "entity.birthDate", target = "birthDate")
    @Mapping(source = "entity.phoneNumber", target = "phoneNumber")
    @Mapping(source = "entity.currentAddress", target = "currentAddress")
    @Mapping(source = "entity.occupation", target = "occupation")
    @Mapping(source = "entity.gender", target = "gender")
    @Mapping(source = "entity.status", target = "status")
    FriendDTO toDTO(User entity);
}
