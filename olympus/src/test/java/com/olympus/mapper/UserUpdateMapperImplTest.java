package com.olympus.mapper;

import com.olympus.dto.request.UserUpdate;
import com.olympus.entity.Gender;
import com.olympus.entity.MaritalStatus;
import com.olympus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserUpdateMapperImplTest {
    private UserUpdateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserUpdateMapper.class);
    }

    @Test
    void whenUpdateEntityFromDTO_thenEntityShouldBeUpdated() {
        // Arrange
        UserUpdate dto = new UserUpdate();
        dto.setFirstName("UpdatedFirstName");
        dto.setLastName("UpdatedLastName");
        dto.setBirthDate("1990-01-01");
        dto.setPhoneNumber("1234567890");
        dto.setCurrentAddress("UpdatedAddress");
        dto.setOccupation("UpdatedOccupation");
        dto.setGender("MALE");
        dto.setStatus("SINGLE");

        User entity = new User(); // Assuming User has a no-arg constructor

        // Act
        mapper.updateEntityFromDTO(dto, entity);

        // Assert
        assertEquals("UpdatedFirstName", entity.getFirstName());
        assertEquals("UpdatedLastName", entity.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), entity.getBirthDate());
        assertEquals("1234567890", entity.getPhoneNumber());
        assertEquals("UpdatedAddress", entity.getCurrentAddress());
        assertEquals("UpdatedOccupation", entity.getOccupation());
        assertEquals(Gender.MALE, entity.getGender()); // Assuming Gender is an enum
        assertEquals(MaritalStatus.SINGLE, entity.getStatus()); // Assuming MaritalStatus is an enum
    }
}