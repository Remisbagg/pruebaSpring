package com.olympus.service.impl;

import com.olympus.dto.response.FriendRequestDTO;
import com.olympus.entity.FriendRequest;
import com.olympus.repository.IFriendRequestRepository;
import com.olympus.service.IFriendRequestService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FriendRequestServiceImpl implements IFriendRequestService {
    private final IFriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendRequestServiceImpl(IFriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public boolean existsByUserId(Long id1, Long id2) {
        return friendRequestRepository.existsFriendRequest(id1, id2) == 1;
    }

    @Override
    public Long createRequest(Long senderId, Long receiverId) {
        FriendRequest friendRequest = new FriendRequest(senderId, receiverId);
        return friendRequestRepository.save(friendRequest).getId();
    }

    @Override
    public boolean isValidDeletePermission(Long userId, Long requestId) {
        return friendRequestRepository.checkDeletePermission(userId, requestId) == 1;
    }

    @Override
    public void deleteRequest(Long requestId) {
        friendRequestRepository.deleteById(requestId);
    }

    @Override
    public boolean validAccepter(Long requestId, Long userId) {
        return friendRequestRepository.existsByIdAndReceiver_Id(requestId, userId);
    }

    @Override
    public FriendRequest findById(long requestId) {
        return friendRequestRepository.getReferenceById(requestId);
    }

    @Override
    public boolean existByRequestId(long id) {
        return friendRequestRepository.existsById(id);
    }

    @Override
    public List<FriendRequestDTO> getListRequestReceived(Long userId) {
        return friendRequestRepository.getListRequestReceived(userId);
    }

    @Override
    public List<FriendRequestDTO> getListRequestSent(Long userId) {
        return friendRequestRepository.getListRequestSent(userId);
    }

    @Override
    public FriendRequest findByUserIds(Long userId1, Long userId2) {
        return friendRequestRepository.findByUserIds(userId1, userId2);
    }

    @Override
    public String identifyRole(Long sourceId, Long targetId) {
        FriendRequest friendRequest = findByUserIds(sourceId, targetId);
        if(friendRequest != null) {
            if(friendRequest.getSender().getId().equals(sourceId)) {
                return "sender";
            }
            return "receiver";
        }
        return null;
    }
}
