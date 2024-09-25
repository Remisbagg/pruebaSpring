package com.back.mapper;

import com.back.dto.response.PostCommentUser;
import com.back.model.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostCommentUserMapper {
    @Mapping(source = "entity.user.id", target = "userId")
    @Mapping(source = "entity.user.email", target = "email")
    @Mapping(source = "entity.user.firstName", target = "firstName")
    @Mapping(source = "entity.user.lastName", target = "lastName")
    @Mapping(source = "entity.user.avatar", target = "avatarUrl")
    PostCommentUser toDTO(PostComment entity);
}
