package com.olympus.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class OtherUserProfile {
    private Long id;
    private OtherUserFriendship friendship;
    private OtherUserFriendRequest friendRequest;
    private String email;
    private String avatar;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String phoneNumber;
    private String currentAddress;
    private String occupation;
    private String gender;
    private String status;
}
