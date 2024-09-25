package com.back.mapper;

import com.back.dto.response.OtherUserProfile;
import com.back.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OtherUserProfileMapper {
    OtherUserProfile toDTO(User user);

    List<OtherUserProfile> toListDTO(List<User> listEntities);
}
