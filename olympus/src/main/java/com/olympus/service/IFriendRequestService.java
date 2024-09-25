package com.olympus.service;

import com.olympus.dto.response.FriendRequestDTO;
import com.olympus.entity.FriendRequest;

import java.util.List;

public interface IFriendRequestService {
    boolean existsByUserId(Long id1, Long id2);
    Long createRequest(Long senderId, Long receiverId);
    boolean isValidDeletePermission(Long userId, Long requestId);
    void deleteRequest(Long requestId);
    boolean validAccepter(Long requestId, Long userId);
    FriendRequest findById(long requestId);
    boolean existByRequestId(long id);
    List<FriendRequestDTO> getListRequestReceived(Long userId);
    List<FriendRequestDTO> getListRequestSent(Long userId);
    FriendRequest findByUserIds(Long userId1, Long userId2);
    String identifyRole(Long sourceId, Long targetId);
}
