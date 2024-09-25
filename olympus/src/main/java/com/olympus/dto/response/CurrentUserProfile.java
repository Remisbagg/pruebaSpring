package com.olympus.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema
public class CurrentUserProfile {
    private Long id;
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
