package com.olympus.dto.response;

import lombok.Data;

@Data
public class OtherUserFriendship {
    private boolean status;
    private Long friendshipId;
    public OtherUserFriendship() {
        this.status = false;
    }
}
