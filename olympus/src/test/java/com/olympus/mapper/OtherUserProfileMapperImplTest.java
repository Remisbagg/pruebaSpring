package com.olympus.mapper;

import com.olympus.dto.response.OtherUserProfile;
import com.olympus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class OtherUserProfileMapperImplTest {
    private OtherUserProfileMapper mapper;
    private User user;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(OtherUserProfileMapper.class);
        user = new User();
        user.setId(1L);
    }

    @Test
    void whenMapUserToDto_thenCorrect() {
        OtherUserProfile dto = mapper.toDTO(user);

        assertEquals(user.getId(), dto.getId());
    }

    @Test
    void whenMapNullUser_thenNull() {
        assertNull(mapper.toDTO(null));
    }
}