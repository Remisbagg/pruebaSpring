package com.olympus.dto.response.friendship;

import com.olympus.entity.Gender;
import com.olympus.entity.MaritalStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FriendDTOTest {
    @Test
    void testFriendDTO() {
        // Arrange
        Long friendshipId = 1L;
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
        FriendDTO friendDTO = new FriendDTO(friendshipId, userId, avatar, firstName, lastName,
                birthDate, phoneNumber, currentAddress, occupation, gender, status);

        // Assert
        assertEquals(friendshipId, friendDTO.getFriendshipId());
        assertEquals(userId, friendDTO.getUserId());
        assertEquals(avatar, friendDTO.getAvatar());
        assertEquals(firstName, friendDTO.getFirstName());
        assertEquals(lastName, friendDTO.getLastName());
        assertEquals(birthDate, friendDTO.getBirthDate());
        assertEquals(phoneNumber, friendDTO.getPhoneNumber());
        assertEquals(currentAddress, friendDTO.getCurrentAddress());
        assertEquals(occupation, friendDTO.getOccupation());
        assertEquals(gender.name(), friendDTO.getGender());
        assertEquals(status.name(), friendDTO.getStatus());
    }
}