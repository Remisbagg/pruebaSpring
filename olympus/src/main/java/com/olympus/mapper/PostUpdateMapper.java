package com.olympus.mapper;

import com.olympus.dto.request.PostUpdate;
import com.olympus.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper
public interface PostUpdateMapper {
    @Mapping(source = "dto.content", target = "entity.content")
    @Mapping(source = "dto.privacy", target = "entity.privacy", qualifiedByName = "mapPrivacy")
    @Mapping(target = "entity.updatedTime", expression = "java(java.time.LocalDateTime.now())")
    void updateEntity(PostUpdate dto, @MappingTarget Post entity);

    @Named("mapPrivacy")
    default String mapPrivacy(String privacy) {
        if (privacy == null) {
            return null;
        }
        return privacy.trim().toUpperCase();
    }
}
