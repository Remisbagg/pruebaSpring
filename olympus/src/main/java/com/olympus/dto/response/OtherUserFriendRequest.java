package com.olympus.dto.response;

import lombok.Data;

@Data
public class OtherUserFriendRequest {
    private boolean status;
    private String role;
    private Long requestId;
    public OtherUserFriendRequest() {
        this.status = false;
    }
}
