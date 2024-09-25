package com.back.dto.request;

import com.back.model.Gender;
import com.back.model.MaritalStatus;
import com.back.validator.annotation.ValidEnum;
import com.back.validator.annotation.ValidLocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema
public class UserUpdate {
    @Schema(example = "Hung")
    @Pattern(regexp = "^(?!\\s*$).+", message = "The first name cannot be blank")
    private String firstName;

    @Schema(example = "Vo")
    @Pattern(regexp = "^(?!\\s*$).+", message = "The last name cannot be blank")
    private String lastName;

    @Schema(example = "1989-03-19")
    @ValidLocalDate
    private String birthDate;

    @Schema(example = "987654321")
    @Pattern(regexp = "^(?!\\s*$).+", message = "The phone number cannot be blank")
    private String phoneNumber;

    @Schema(example = "Hanoi")
    @Pattern(regexp = "^(?!\\s*$).+", message = "The address cannot be blank")
    private String currentAddress;

    @Schema(example = "Programmer")
    @Pattern(regexp = "^(?!\\s*$).+", message = "The occupation cannot be blank")
    private String occupation;

    @Schema(example = "MALE")
    @ValidEnum(enumClass = Gender.class, message = "The gender is not valid")
    private String gender;

    @Schema(example = "MARRIED")
    @ValidEnum(enumClass = MaritalStatus.class, message = "The status is not valid")
    private String status;
}
