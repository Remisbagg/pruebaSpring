package com.olympus.controller;

import com.olympus.config.Constant;
import com.olympus.dto.request.UserUpdate;
import com.olympus.dto.response.BaseResponse;
import com.olympus.dto.response.CurrentUserProfile;
import com.olympus.dto.response.OtherUserProfile;
import com.olympus.service.IUserService;
import com.olympus.validator.AppValidator;
import com.olympus.validator.annotation.user.ExistUserById;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin("*")
@Tag(name = "3. Profile", description = "User's Profile Management APIs")
@Validated
public class ProfileController {
    private final IUserService userService;
    private final AppValidator appValidator;

    @Autowired
    public ProfileController(IUserService userService,
                             AppValidator validator) {
        this.userService = userService;
        this.appValidator = validator;
    }

    @GetMapping("/{userId}/profile")
    @Operation(summary = "Get user's profiles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable @Valid @ExistUserById Long userId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (loggedInUserId.equals(userId)) {
            CurrentUserProfile data = userService.getCurrentUserProfile(userId);
            BaseResponse<CurrentUserProfile, ?> response =
                    BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_PROFILE_GET, data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        OtherUserProfile data = userService.getOtherUserProfile(loggedInUserId, userId);
        BaseResponse<OtherUserProfile, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_PROFILE_GET, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search user by name or email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    ResponseEntity<?> searchUsers(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam @Valid @NotBlank String keyword) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        List<OtherUserProfile> data = userService.searchUsers(keyword, loggedInUserId);
        BaseResponse<List<OtherUserProfile>, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_PROFILE_GET, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping(
            value = "/{userId}/profile",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(summary = "Update user's profiles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable @Valid @ExistUserById Long userId,
                                           @RequestPart @Valid UserUpdate user,
                                           @RequestPart(required = false)
                                           MultipartFile file) throws IOException {
        ResponseEntity<?> validationError = appValidator.validateUserUpdate(userDetails, userId);
        if (validationError != null) {
            return validationError;
        }
        appValidator.validateImgFile(file);
        Long updateId = userService.updateUser(userId, user, file);
        Map<String, Long> data = new HashMap<>();
        data.put("updatedUserId", updateId);
        BaseResponse<Map<String, Long>, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_USER_UPDATE, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
