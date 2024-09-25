package com.olympus.mapper;

import com.olympus.dto.response.friendship.FriendDTO;
import com.olympus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class FriendMapperImplTest {
    private FriendMapper mapper;
    private User user;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(FriendMapper.class);
        user = new User(1L);
    }

    @Test
    void whenMapUserToDto_thenCorrect() {
        FriendDTO dto = mapper.toDTO(user);

        assertEquals(user.getId(), dto.getUserId());
    }

    @Test
    void whenMapNullUser_thenNull() {
        assertNull(mapper.toDTO(null));
    }
}