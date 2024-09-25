package com.back.mapper;

import com.back.dto.response.GetPostImage;
import com.back.model.PostImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper
public interface GetPostImageMapper {
    @Mapping(source = "entity.id", target = "imageId")
    @Mapping(source = "entity.url", target = "url")
    @Mapping(source = "entity.post.id", target = "postId")
    GetPostImage entityToDto(PostImage entity);
}
