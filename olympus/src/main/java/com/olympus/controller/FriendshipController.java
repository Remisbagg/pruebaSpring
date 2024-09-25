package com.olympus.controller;

import com.olympus.config.Constant;
import com.olympus.dto.response.BaseResponse;
import com.olympus.dto.response.friendship.FriendDTO;
import com.olympus.service.IFriendshipService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/friendship")
@CrossOrigin("*")
@Tag(name = "5. Friendship", description = "User's friendship Management APIs")
@Validated
public class FriendshipController {
    private final IFriendshipService friendshipService;
    private final IUserService userService;
    private final AppValidator appValidator;

    @Autowired
    public FriendshipController(IFriendshipService friendshipService,
                                IUserService userService,
                                AppValidator appValidator) {
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.appValidator = appValidator;
    }

    @GetMapping("/friends-list")
    @Operation(summary = "Get an user's friend list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> getFriendsList(@AuthenticationPrincipal
                                            UserDetails userDetails) {
        Long userId = userService.findIdByUserDetails(userDetails);
        List<FriendDTO> data = friendshipService.getFriendsList(userId);
        BaseResponse<List<FriendDTO>, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_FRIENDSHIP_GET_LIST, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Unfriend")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> unFriend(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable @Valid @ExistUserById Long userId) {
        ResponseEntity<?> validationError = appValidator.validateUnFriend(userDetails, userId);
        if (validationError != null) {
            return validationError;
        }
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        friendshipService.unFriend(loggedInUserId, userId);
        BaseResponse<String, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_FRIENDSHIP_DELETE, HttpStatus.NO_CONTENT.name());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
