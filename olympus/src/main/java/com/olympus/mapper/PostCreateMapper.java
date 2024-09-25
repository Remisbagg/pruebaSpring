package com.olympus.mapper;

import com.olympus.dto.request.PostCreate;
import com.olympus.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface PostCreateMapper {

    @Mapping(target = "content", source = "content")
    @Mapping(target = "deleteStatus", constant = "false")
    @Mapping(target = "privacy", source = "privacy", qualifiedByName = "mapPrivacy")
    @Mapping(target = "createdTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedTime", expression = "java(java.time.LocalDateTime.now())")
    Post toPost(PostCreate postCreate);

    @Named("mapPrivacy")
    default String mapPrivacy(String privacy) {
        if (privacy == null) {
            return null;
        }
        return privacy.trim().toUpperCase();
    }
}
