package com.olympus.mapper;

import com.olympus.dto.response.PostCommentUser;
import com.olympus.dto.response.newsfeed.PostCommentDTO;
import com.olympus.entity.PostComment;
import com.olympus.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface PostCommentMapper {
    @Mapping(source = "entity.id", target = "commentId")
    @Mapping(source = "entity.user", target = "user", qualifiedByName = "mapUser")
    @Mapping(source = "entity.content", target = "content")
    @Mapping(source = "entity.createdTime", target = "createdTime")
    @Mapping(source = "entity.updatedTime", target = "updatedTime")
    PostCommentDTO toDTO(PostComment entity);

    @Named("mapUser")
    default PostCommentUser mapUser(User user) {
        if(user == null) {
            return null;
        }
        PostCommentUser postCommentUser =  new PostCommentUser();
        postCommentUser.setUserId(user.getId());
        postCommentUser.setEmail(user.getEmail());
        postCommentUser.setFirstName(user.getFirstName());
        postCommentUser.setLastName(user.getLastName());
        postCommentUser.setAvatarUrl(user.getAvatar());

        return postCommentUser;
    }

    List<PostCommentDTO> toListDTO(List<PostComment> listEntity);
}
