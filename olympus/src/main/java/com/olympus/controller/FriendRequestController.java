package com.olympus.controller;

import com.olympus.config.Constant;
import com.olympus.dto.response.BaseResponse;
import com.olympus.dto.response.FriendRequestDTO;
import com.olympus.service.IFriendRequestService;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IUserService;
import com.olympus.validator.AppValidator;
import com.olympus.validator.annotation.friendRequest.ExistById;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1/friends/requests")
@Tag(name = "4. Friend Request", description = "Friend Request Management APIs")
@Validated
public class FriendRequestController {
    private final AppValidator appValidator;
    private final IFriendRequestService friendRequestService;
    private final IFriendshipService friendshipService;
    private final IUserService userService;

    @Autowired
    public FriendRequestController(AppValidator appValidator,
                                   IFriendRequestService friendRequestService,
                                   IFriendshipService friendshipService,
                                   IUserService userService) {
        this.appValidator = appValidator;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    @GetMapping("/received")
    @Operation(summary = "Get list friend request received")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<?> geListRequestReceived(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findIdByUserDetails(userDetails);
        List<FriendRequestDTO> data = friendRequestService.getListRequestReceived(userId);
        BaseResponse<List<FriendRequestDTO>, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_FRIEND_REQUEST_RECEIVER, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/sent")
    @Operation(summary = "Get list friend request sent")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<?> getListRequestSent(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findIdByUserDetails(userDetails);
        List<FriendRequestDTO> data = friendRequestService.getListRequestSent(userId);
        BaseResponse<List<FriendRequestDTO>, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_FRIEND_REQUEST_SENT, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/sent/{targetUserId}")
    @Operation(summary = "Send a friend request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> sendFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable @Valid @ExistUserById Long targetUserId) {
        ResponseEntity<?> validationError = appValidator.validateFriendRequestSent(userDetails, targetUserId);
        if (validationError != null) {
            return validationError;
        }
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        Long requestId = friendRequestService.createRequest(loggedInUserId, targetUserId);
        Map<String, Long> data = new HashMap<>();
        data.put("friendRequestId", requestId);
        BaseResponse<Map<String, Long>, ?> response =
                BaseResponse.success(HttpStatus.CREATED, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_FRIEND_REQUEST_CREATE, data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "Cancel a friend request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> cancelFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable @Valid @ExistById Long requestId) {
        ResponseEntity<?> validationError = appValidator.validateFriendRequestDelete(userDetails, requestId);
        if (validationError != null) {
            return validationError;
        }
        friendRequestService.deleteRequest(requestId);
        BaseResponse<String, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_FRIEND_REQUEST_DELETE, HttpStatus.NO_CONTENT.name());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/accept/{requestId}")
    @Operation(summary = "Confirm a friend request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class),
                            mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> acceptFriendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable @Valid @ExistById Long requestId) {
        ResponseEntity<?> validationError = appValidator.validateFriendRequestAccept(userDetails, requestId);
        if (validationError != null) {
            return validationError;
        }
        Long friendshipId = friendshipService.create(requestId);
        Map<String, Long> data = new HashMap<>();
        data.put("friendshipId", friendshipId);
        BaseResponse<Map<String, Long>, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_FRIEND_REQUEST_CONFIRM, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
