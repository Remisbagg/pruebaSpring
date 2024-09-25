package com.back.service;

import com.back.dto.response.friendship.FriendDTO;
import com.back.model.Friendship;

import java.util.List;

public interface IFriendshipService {
    boolean existsFriendship(Long id1, Long id2);
    Long create(Long friendRequestId);
    List<FriendDTO> getFriendsList(Long userId);
    List<Long> getListFriendIds(Long userId);
    Friendship findByUserIds(Long id1, Long id2);
    void unFriend(Long id1, Long id2);
}
