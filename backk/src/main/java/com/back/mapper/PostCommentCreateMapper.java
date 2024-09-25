package com.back.mapper;

import com.back.dto.request.PostCommentCreate;
import com.back.model.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostCommentCreateMapper {
    @Mapping(source = "dto.content", target = "content")
    @Mapping(target = "createdTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deleteStatus", constant = "false")
    PostComment dtoToEntity(PostCommentCreate dto);
}
