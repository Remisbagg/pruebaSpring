package com.olympus.mapper;

import com.olympus.dto.response.OtherUserProfile;
import com.olympus.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OtherUserProfileMapper {
    OtherUserProfile toDTO(User user);

    List<OtherUserProfile> toListDTO(List<User> listEntities);
}
