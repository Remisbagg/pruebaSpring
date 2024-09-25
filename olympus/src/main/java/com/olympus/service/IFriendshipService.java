package com.olympus.service;

import com.olympus.dto.response.friendship.FriendDTO;
import com.olympus.entity.Friendship;

import java.util.List;

public interface IFriendshipService {
    boolean existsFriendship(Long id1, Long id2);
    Long create(Long friendRequestId);
    List<FriendDTO> getFriendsList(Long userId);
    List<Long> getListFriendIds(Long userId);
    Friendship findByUserIds(Long id1, Long id2);
    void unFriend(Long id1, Long id2);
}
