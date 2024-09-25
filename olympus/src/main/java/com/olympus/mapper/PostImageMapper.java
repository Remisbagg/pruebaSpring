package com.olympus.mapper;

import com.olympus.dto.response.newsfeed.PostImageDTO;
import com.olympus.entity.PostImage;
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
