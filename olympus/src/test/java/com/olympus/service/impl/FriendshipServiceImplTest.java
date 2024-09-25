package com.olympus.service.impl;

import com.olympus.dto.response.friendship.FriendDTO;
import com.olympus.entity.FriendRequest;
import com.olympus.entity.Friendship;
import com.olympus.entity.User;
import com.olympus.repository.IFriendshipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceImplTest {
    @InjectMocks
    private FriendshipServiceImpl friendshipService;
    @Mock
    private FriendRequestServiceImpl friendRequestService;
    @Mock
    private IFriendshipRepository friendshipRepository;

    @Test
    public void testExistFriendship_Exist() {
        //Arrange
        Long id1 = 1L;
        Long id2 = 2L;
        User user1 = new User(id1);
        User user2 = new User(id2);
        when(friendshipRepository.existsFriendship(user1, user2)).thenReturn(1L);

        //Act
        boolean exist = friendshipService.existsFriendship(1L, 2L);

        //Assert
        assertTrue(exist);
    }

    @Test
    public void testExistFriendship_NotExist() {
        //Arrange
        Long id1 = 1L;
        Long id2 = 2L;
        User user1 = new User(id1);
        User user2 = new User(id2);
        when(friendshipRepository.existsFriendship(user1, user2)).thenReturn(0L);

        //Act
        boolean exist = friendshipService.existsFriendship(1L, 2L);

        //Assert
        assertFalse(exist);
    }

    @Test
    public void testCreate_Success() {
        // Arrange
        long friendRequestId = 1L;
        FriendRequest friendRequest = new FriendRequest(new User(1L).getId(), new User(2L).getId());
        when(friendRequestService.findById(friendRequestId)).thenReturn(friendRequest);
        Friendship mockFriendship = new Friendship();
        mockFriendship.setId(1L);
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(mockFriendship);

        // Act
        Long friendshipId = friendshipService.create(friendRequestId);

        // Assert
        assertNotNull(friendshipId);
        assertEquals(1L, friendshipId);
    }

    @Test
    public void testGetFriendsList_NotEmpty() {
        // Arrange
        Long userId = 1L;
        when(friendshipRepository.getListFriendAsReceiver(userId)).thenReturn(List.of(new FriendDTO()));
        when(friendshipRepository.getListFriendAsSender(userId)).thenReturn(List.of(new FriendDTO()));

        // Act
        List<FriendDTO> friendsList = friendshipService.getFriendsList(userId);

        // Assert
        assertFalse(friendsList.isEmpty());
    }

    @Test
    public void testGetFriendsList_Empty() {
        // Arrange
        Long userId = 1L;
        when(friendshipRepository.getListFriendAsReceiver(userId)).thenReturn(Collections.emptyList());
        when(friendshipRepository.getListFriendAsSender(userId)).thenReturn(Collections.emptyList());

        // Act
        List<FriendDTO> friendsList = friendshipService.getFriendsList(userId);

        // Assert
        assertTrue(friendsList.isEmpty());
    }

    @Test
    public void testGetListFriendIds_NotEmpty() {
        // Arrange
        Long userId = 1L;
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setFriendshipId(1L);
        friendDTO.setUserId(2L);
        List<FriendDTO> mockList = List.of(friendDTO);
        when(friendshipRepository.getListFriendAsReceiver(userId)).thenReturn(mockList);
        when(friendshipRepository.getListFriendAsSender(userId)).thenReturn(mockList);

        // Act
        List<Long> friendIds = friendshipService.getListFriendIds(userId);

        // Assert
        assertFalse(friendIds.isEmpty());
    }

    @Test
    public void testGetListFriendIds_Empty() {
        // Arrange
        Long userId = 1L;
        when(friendshipService.getFriendsList(userId)).thenReturn(Collections.emptyList());

        // Act
        List<Long> friendIds = friendshipService.getListFriendIds(userId);

        // Assert
        assertTrue(friendIds.isEmpty());
    }

    @Test
    public void testFindByUserIds_Found() {
        // Arrange
        Friendship mockFriendship = new Friendship();
        when(friendshipRepository.findByUserIds(1L, 2L)).thenReturn(mockFriendship);

        // Act
        Friendship foundFriendship = friendshipService.findByUserIds(1L, 2L);

        // Assert
        assertNotNull(foundFriendship);
    }

    @Test
    public void testFindByUserIds_NotFound() {
        // Arrange
        when(friendshipRepository.findByUserIds(1L, 2L)).thenReturn(null);

        // Act
        Friendship foundFriendship = friendshipService.findByUserIds(1L, 2L);

        // Assert
        assertNull(foundFriendship);
    }

    @Test
    public void testUnFriend() {
        // Arrange
        Friendship mockFriendship = new Friendship();
        when(friendshipRepository.findByUserIds(1L, 2L)).thenReturn(mockFriendship);

        // Act
        friendshipService.unFriend(1L, 2L);

        // Assert
        // Verify that delete was called with the correct friendship
        verify(friendshipRepository).delete(mockFriendship);
    }
}