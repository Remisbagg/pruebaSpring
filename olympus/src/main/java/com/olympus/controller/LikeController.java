package com.olympus.controller;

import com.olympus.config.Constant;
import com.olympus.dto.response.BaseResponse;
import com.olympus.service.IPostLikeService;
import com.olympus.service.IUserService;
import com.olympus.utils.RealTimeMessenger;
import com.olympus.validator.AppValidator;
import com.olympus.validator.annotation.post.ExistByPostIdAndNotDeleted;
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

@RestController
@RequestMapping
@CrossOrigin("*")
@Tag(name = "8. Post Likes", description = "Post's Likes Management APIs")
@Validated
public class LikeController {
    private final AppValidator appValidator;
    private final IPostLikeService postLikeService;
    private final IUserService userService;
    private final RealTimeMessenger messenger;

    @Autowired
    public LikeController(AppValidator appValidator, IPostLikeService postLikeService,
                          IUserService userService, RealTimeMessenger messenger) {
        this.appValidator = appValidator;
        this.postLikeService = postLikeService;
        this.userService = userService;
        this.messenger = messenger;
    }

    @PostMapping(value = "/v1/users/{userId}/posts/{postId}")
    @Operation(summary = "Like or unlike a post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})})
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> likerOrUnlike(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable @Valid Long userId,
                                           @PathVariable @Valid @ExistByPostIdAndNotDeleted Long postId) {
        ResponseEntity<?> validationError = appValidator.validateLikeOrUnlikeAction(userDetails, userId, postId);
        if (validationError != null) {
            return validationError;
        }
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        boolean like = postLikeService.likeOrUnlikePost(loggedInUserId, postId);
        messenger.broadcastPostLikeAction(postId, like, loggedInUserId);
        BaseResponse<String, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_LIKE_UNLIKE, HttpStatus.NO_CONTENT.name());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
