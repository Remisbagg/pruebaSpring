package com.olympus.mapper;

import com.olympus.dto.request.FriendRequestSent;
import com.olympus.entity.FriendRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FriendRequestSentMapperImplTest {
    private FriendRequestSentMapper mapper;

    @BeforeEach
    public void setUp() {
        // Get an instance of the mapper
        mapper = Mappers.getMapper(FriendRequestSentMapper.class);
    }

    @Test
    void whenMapDtoToEntity_thenCorrectMapping() {
        // Arrange
        FriendRequestSent dto = new FriendRequestSent();
        dto.setReceiverId(1L);

        // Act
        FriendRequest entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertNotNull(entity.getReceiver());
        assertEquals(dto.getReceiverId(), entity.getReceiver().getId());
    }

    @Test
    void whenDtoIsNull_thenReturnNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void whenUserIsNull_thenReturnNull() {
        // Arrange
        FriendRequestSent dto = new FriendRequestSent();
        dto.setReceiverId(null);
        // Act
        FriendRequest entity = mapper.toEntity(dto);
        // Assert
        assertNull(entity.getReceiver());
    }

    @Test
    void whenReceiverIdIsNull_thenReceiverIsNull() {
        // Arrange
        FriendRequestSent dto = new FriendRequestSent(); // receiverId not set

        // Act
        FriendRequest entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getReceiver());
    }
}