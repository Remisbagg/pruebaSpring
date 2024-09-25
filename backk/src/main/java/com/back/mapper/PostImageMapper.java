package com.back.mapper;

import com.back.dto.response.newsfeed.PostImageDTO;
import com.back.model.PostImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PostImageMapper {
    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.url", target = "url")
    PostImageDTO toDTO(PostImage entity);

    List<PostImageDTO> toDTOList(List<PostImage> listEntity);
}
