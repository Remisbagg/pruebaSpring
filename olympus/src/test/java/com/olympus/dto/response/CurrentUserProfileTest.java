package com.olympus.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CurrentUserProfileTest {
    @Test
    void testCurrentUserProfile() {
        // Arrange
        Long id = 1L;
        String email = "user@example.com";
        String avatar = "http://example.com/avatar.jpg";
        String firstName = "Hung";
        String lastName = "Vo";
        LocalDate birthDate = LocalDate.of(1989, 3, 19);
        String phoneNumber = "1234567890";
        String currentAddress = "123 Hanoi";
        String occupation = "Engineer";
        String gender = "Male";
        String status = "Single";

        // Act
        CurrentUserProfile profile = new CurrentUserProfile();
        profile.setId(id);
        profile.setEmail(email);
        profile.setAvatar(avatar);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setBirthDate(birthDate);
        profile.setPhoneNumber(phoneNumber);
        profile.setCurrentAddress(currentAddress);
        profile.setOccupation(occupation);
        profile.setGender(gender);
        profile.setStatus(status);

        // Assert
        assertEquals(id, profile.getId());
        assertEquals(email, profile.getEmail());
        assertEquals(avatar, profile.getAvatar());
        assertEquals(firstName, profile.getFirstName());
        assertEquals(lastName, profile.getLastName());
        assertEquals(birthDate, profile.getBirthDate());
        assertEquals(phoneNumber, profile.getPhoneNumber());
        assertEquals(currentAddress, profile.getCurrentAddress());
        assertEquals(occupation, profile.getOccupation());
        assertEquals(gender, profile.getGender());
        assertEquals(status, profile.getStatus());
    }
}