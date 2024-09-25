package com.olympus.service.impl;

import com.olympus.dto.response.friendship.FriendDTO;
import com.olympus.entity.FriendRequest;
import com.olympus.entity.Friendship;
import com.olympus.entity.User;
import com.olympus.repository.IFriendshipRepository;
import com.olympus.service.IFriendRequestService;
import com.olympus.service.IFriendshipService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class FriendshipServiceImpl implements IFriendshipService {
    private final IFriendshipRepository friendshipRepository;
    private final IFriendRequestService friendRequestService;
    @Autowired
    public FriendshipServiceImpl(IFriendshipRepository friendshipRepository,
                                 IFriendRequestService friendRequestService) {
        this.friendshipRepository = friendshipRepository;
        this.friendRequestService = friendRequestService;
    }

    @Override
    public boolean existsFriendship(Long id1, Long id2) {
        User user1 = new User(id1);
        User user2 = new User(id2);
        return friendshipRepository.existsFriendship(user1, user2) != 0;
    }

    @Override
    public Long create(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestService.findById(friendRequestId);
        Friendship friendship = new Friendship();
        friendship.setUser1(friendRequest.getSender());
        friendship.setUser2(friendRequest.getReceiver());
        friendship.setCreatedTime(LocalDate.now());
        friendRequestService.deleteRequest(friendRequestId);
        return friendshipRepository.save(friendship).getId();
    }

    @Override
    public List<FriendDTO> getFriendsList(Long userId) {
        List<FriendDTO> list1 = friendshipRepository.getListFriendAsReceiver(userId);
        List<FriendDTO> list2 = friendshipRepository.getListFriendAsSender(userId);
        return Stream.of(list1,list2)
                .filter(Objects::nonNull).flatMap(Collection::stream)
                .toList();
    }

    @Override
    public List<Long> getListFriendIds(Long userId) {
        if(getFriendsList(userId).isEmpty()) {
            return Collections.emptyList();
        }
        return getFriendsList(userId).stream()
                .map(FriendDTO::getUserId)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Friendship findByUserIds(Long id1, Long id2) {
        return friendshipRepository.findByUserIds(id1,id2);
    }

    @Override
    public void unFriend(Long id1, Long id2) {
        Friendship friendship = findByUserIds(id1, id2);
        friendshipRepository.delete(friendship);
    }
}
