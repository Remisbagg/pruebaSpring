package com.olympus.dto.response;

import com.olympus.entity.Gender;
import com.olympus.entity.MaritalStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FriendRequestDTOTest {
    @Test
    void testFriendRequestDTO() {
        // Arrange
        Long friendRequestId = 1L;
        Long userId = 2L;
        String avatar = "http://example.com/avatar.jpg";
        String firstName = "Hung";
        String lastName = "Vo";
        LocalDate birthDate = LocalDate.of(1989, 3, 19);
        String phoneNumber = "1234567890";
        String currentAddress = "123 Hanoi";
        String occupation = "Engineer";
        Gender gender = Gender.MALE;
        MaritalStatus status = MaritalStatus.SINGLE;

        // Act
        FriendRequestDTO friendRequestDTO = new FriendRequestDTO(friendRequestId, userId, avatar,
                firstName, lastName, birthDate, phoneNumber, currentAddress, occupation, gender, status);

        // Assert
        assertEquals(friendRequestId, friendRequestDTO.getFriendRequestId());
        assertEquals(userId, friendRequestDTO.getUserId());
        assertEquals(avatar, friendRequestDTO.getAvatar());
        assertEquals(firstName, friendRequestDTO.getFirstName());
        assertEquals(lastName, friendRequestDTO.getLastName());
        assertEquals(birthDate, friendRequestDTO.getBirthDate());
        assertEquals(phoneNumber, friendRequestDTO.getPhoneNumber());
        assertEquals(currentAddress, friendRequestDTO.getCurrentAddress());
        assertEquals(occupation, friendRequestDTO.getOccupation());
        assertEquals(gender.name(), friendRequestDTO.getGender());
        assertEquals(status.name(), friendRequestDTO.getStatus());
    }
}