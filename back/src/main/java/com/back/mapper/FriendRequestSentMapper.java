package com.back.mapper;

import com.back.dto.request.FriendRequestSent;
import com.back.entity.FriendRequest;
import com.back.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface FriendRequestSentMapper {
    @Mapping(source = "dto.receiverId", target = "receiver", qualifiedByName = "mapUser")
    FriendRequest toEntity(FriendRequestSent dto);

    @Named("mapUser")
    default User mapUser(String userId){
        if (userId == null) {
            return null;
        }
        return new User(userId);
    }
}
