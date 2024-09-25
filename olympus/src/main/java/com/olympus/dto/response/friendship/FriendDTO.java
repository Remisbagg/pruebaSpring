package com.olympus.dto.response.friendship;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olympus.entity.Gender;
import com.olympus.entity.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Schema
@NoArgsConstructor
public class FriendDTO {
    private Long friendshipId;
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

    public FriendDTO(Long friendshipId, Long userId, String avatar, String firstName,
                     String lastName, LocalDate birthDate, String phoneNumber,
                     String currentAddress, String occupation, Gender gender, MaritalStatus status) {
        this.friendshipId = friendshipId;
        this.userId = userId;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.currentAddress = currentAddress;
        this.occupation = occupation;
        this.gender = gender != null? gender.name():Gender.RATHER_NOT_TO_SAY.name();
        this.status = status != null? status.name(): MaritalStatus.RATHER_NOT_TO_SAY.name();
    }
}
