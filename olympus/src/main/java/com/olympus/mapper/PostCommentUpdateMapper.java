package com.olympus.mapper;

import com.olympus.dto.request.PostCommentUpdate;
import com.olympus.entity.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PostCommentUpdateMapper {
    @Mapping(source = "dto.content", target = "content")
    @Mapping(target = "entity.createdTime", ignore = true)
    @Mapping(target = "entity.updatedTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleteStatus", constant = "false")
    void updateEntityFromDTO(PostCommentUpdate dto, @MappingTarget PostComment entity);
}
