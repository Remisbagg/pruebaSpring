package com.olympus.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class OtherUserFriendRequestTest {
    @Test
    void testDefaultConstructor() {
        // Act
        OtherUserFriendRequest request = new OtherUserFriendRequest();

        // Assert
        assertFalse(request.isStatus()); // default status should be false as set in constructor
    }

    @Test
    void testFieldSettersAndGetters() {
        // Arrange
        boolean status = true;
        String role = "Sender";
        Long requestId = 1L;

        // Act
        OtherUserFriendRequest request = new OtherUserFriendRequest();
        request.setStatus(status);
        request.setRole(role);
        request.setRequestId(requestId);

        // Assert
        assertEquals(status, request.isStatus());
        assertEquals(role, request.getRole());
        assertEquals(requestId, request.getRequestId());
    }
}