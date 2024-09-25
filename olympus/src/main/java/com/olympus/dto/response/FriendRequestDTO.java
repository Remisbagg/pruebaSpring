package com.olympus.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olympus.entity.Gender;
import com.olympus.entity.MaritalStatus;
import lombok.Data;

import java.time.LocalDate;
@Data
public class FriendRequestDTO {
    private Long friendRequestId;
    private Long userId;
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

    public FriendRequestDTO(){}

    public FriendRequestDTO(Long friendRequestId, Long userId, String avatar,
                            String firstName, String lastName, LocalDate birthDate,
                            String phoneNumber, String currentAddress, String occupation,
                            Gender gender, MaritalStatus status) {
        this.friendRequestId = friendRequestId;
        this.userId = userId;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.currentAddress = currentAddress;
        this.occupation = occupation;
        this.gender = gender.name();
        this.status = status.name();
    }
}
