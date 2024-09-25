package com.olympus.mapper;

import com.olympus.dto.response.CurrentUserProfile;
import com.olympus.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface CurrentUserProfileMapper {
    CurrentUserProfile toDTO(User entity);
}
