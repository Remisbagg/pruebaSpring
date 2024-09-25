package com.back.mapper;

import com.back.dto.response.CurrentUserProfile;
import com.back.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface CurrentUserProfileMapper {
    CurrentUserProfile toDTO(User entity);
}
