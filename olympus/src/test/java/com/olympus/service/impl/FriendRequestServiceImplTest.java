package com.olympus.service.impl;

import com.olympus.dto.response.FriendRequestDTO;
import com.olympus.entity.FriendRequest;
import com.olympus.repository.IFriendRequestRepository;
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
class FriendRequestServiceImplTest {
    @InjectMocks
    private FriendRequestServiceImpl friendRequestService;

    @Mock
    private IFriendRequestRepository friendRequestRepository;

    @Test
    public void testExistsByUserId_Exist() {
        //Arrange
        Long id1 = 1L;
        Long id2 = 2L;
        when(friendRequestRepository.existsFriendRequest(id1, id2))
                .thenReturn(1L);

        //Act
        boolean exist = friendRequestService.existsByUserId(id1, id2);

        //Assert
        assertTrue(exist);
    }

    @Test
    public void testExistsByUserId_NotExist() {
        //Arrange
        Long id1 = 1L;
        Long id2 = 2L;
        when(friendRequestRepository.existsFriendRequest(id1, id2))
                .thenReturn(0L);

        //Act
        boolean exist = friendRequestService.existsByUserId(id1, id2);

        //Assert
        assertFalse(exist);
    }


    @Test
    public void testCreateRequest_Success() {
        //Arrange
        Long senderId = 1L;
        Long receiverId = 2L;
        FriendRequest mockFriendRequest = new FriendRequest(senderId, receiverId);
        mockFriendRequest.setId(3L);
        when(friendRequestRepository.save(any(FriendRequest.class))).thenReturn(mockFriendRequest);

        //Act
        Long requestId = friendRequestService.createRequest(senderId, receiverId);

        //Assert
        assertEquals(mockFriendRequest.getId(), requestId);
    }

    @Test
    public void isValidDeletePermission_Valid() {
        //Arrange
        Long userId = 1L;
        Long requestId = 2L;
        when(friendRequestRepository.checkDeletePermission(userId, requestId))
                .thenReturn(1L);

        //Act
        boolean valid = friendRequestService.isValidDeletePermission(1L, 2L);

        //Assert
        assertTrue(valid);
    }

    @Test
    public void isValidDeletePermission_Invalid() {
        //Arrange
        Long userId = 1L;
        Long requestId = 2L;
        when(friendRequestRepository.checkDeletePermission(userId, requestId))
                .thenReturn(0L);

        //Act
        boolean valid = friendRequestService.isValidDeletePermission(1L, 2L);

        //Assert
        assertFalse(valid);
    }

    @Test
    public void deleteRequest() {
        // Arrange
        Long requestId = 1L;

        // Act
        friendRequestService.deleteRequest(requestId);

        // Assert
        // Verify deleteById was called with the correct ID
        verify(friendRequestRepository).deleteById(requestId);

    }

    @Test
    public void testValidAccepter_Valid() {
        //Arrange
        when(friendRequestRepository.existsByIdAndReceiver_Id(1L, 1L)).thenReturn(true);

        // Act
        boolean isValid = friendRequestService.validAccepter(1L, 1L);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testValidAccepter_Invalid() {
        // Arrange
        when(friendRequestRepository.existsByIdAndReceiver_Id(1L, 1L)).thenReturn(false);

        // Act
        boolean isValid = friendRequestService.validAccepter(1L, 1L);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void findById() {
        // Arrange
        FriendRequest friendRequest = new FriendRequest(); // setup your friendRequest object
        when(friendRequestRepository.getReferenceById(1L)).thenReturn(friendRequest);

        // Act
        FriendRequest found = friendRequestService.findById(1L);

        // Assert
        assertNotNull(found);
    }

    @Test
    public void existByRequestId_Exist() {
        // Arrange
        when(friendRequestRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean exists = friendRequestService.existByRequestId(1L);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistByRequestId_NotExist() {
        // Arrange
        when(friendRequestRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean exists = friendRequestService.existByRequestId(1L);

        // Assert
        assertFalse(exists);
    }

    @Test
    public void getListRequestReceived() {
        // Arrange
        when(friendRequestRepository.getListRequestReceived(1L)).thenReturn(Collections.singletonList(new FriendRequestDTO()));

        // Act
        List<FriendRequestDTO> received = friendRequestService.getListRequestReceived(1L);

        // Assert
        assertFalse(received.isEmpty());
    }

    @Test
    public void getListRequestSent() {
        // Arrange
        when(friendRequestRepository.getListRequestSent(1L)).thenReturn(Collections.singletonList(new FriendRequestDTO()));

        // Act
        List<FriendRequestDTO> sent = friendRequestService.getListRequestSent(1L);

        // Assert
        assertFalse(sent.isEmpty());
    }

    @Test
    public void testFindByUserIds() {
        // Arrange
        FriendRequest friendRequest = new FriendRequest(); // setup your friendRequest object
        when(friendRequestRepository.findByUserIds(1L, 2L)).thenReturn(friendRequest);

        // Act
        FriendRequest found = friendRequestService.findByUserIds(1L, 2L);

        // Assert
        assertNotNull(found);
    }

    @Test
    public void testIdentifyRole_Sender() {
        // Arrange
        FriendRequest friendRequest = new FriendRequest(1L, 2L);
        when(friendRequestRepository.findByUserIds(1L, 2L)).thenReturn(friendRequest);
        // Mock more behavior if needed

        // Act
        String role = friendRequestService.identifyRole(1L, 2L);

        // Assert
        assertEquals("sender", role);
    }

    @Test
    public void testIdentifyRole_Receiver() {
        // Arrange
        FriendRequest friendRequest = new FriendRequest(2L, 1L);
        when(friendRequestRepository.findByUserIds(1L, 2L)).thenReturn(friendRequest);
        // Mock more behavior if needed

        // Act
        String role = friendRequestService.identifyRole(1L, 2L);

        // Assert
        assertEquals("receiver", role);
    }

    @Test
    public void testIdentifyRole_Null() {
        // Arrange
        when(friendRequestRepository.findByUserIds(1L, 2L)).thenReturn(null);

        // Act
        String role = friendRequestService.identifyRole(1L, 2L);

        // Assert
        assertNull(role);
    }
}