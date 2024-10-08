package com.back.dto.request;

import com.back.validator.annotation.user.ExistUserById;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Schema
@Setter
public class FriendRequestSent {
    @Schema(example = "2")
    @ExistUserById
    private Long receiverId;
}
