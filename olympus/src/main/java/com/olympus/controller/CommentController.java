package com.olympus.controller;

import com.olympus.config.Constant;
import com.olympus.dto.request.PostCommentCreate;
import com.olympus.dto.request.PostCommentUpdate;
import com.olympus.dto.response.BaseResponse;
import com.olympus.service.IPostCommentService;
import com.olympus.service.IUserService;
import com.olympus.utils.RealTimeMessenger;
import com.olympus.validator.AppValidator;
import com.olympus.validator.annotation.post.ExistByPostIdAndNotDeleted;
import com.olympus.validator.annotation.postcomment.ExistByCommentIdAndNotDeleted;
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
import java.util.Map;

@RestController
@RequestMapping("/v1/users/{userId}")
@CrossOrigin("*")
@Tag(name = "7. Comment", description = "Post's Comments Management APIs")
@Validated
public class CommentController {
    private final AppValidator appValidator;
    private final IPostCommentService postCommentService;
    private final IUserService userService;
    private final RealTimeMessenger messenger;

    @Autowired
    public CommentController(AppValidator appValidator,
                             IPostCommentService postCommentService,
                             IUserService userService,
                             RealTimeMessenger messenger) {
        this.appValidator = appValidator;
        this.postCommentService = postCommentService;
        this.userService = userService;
        this.messenger = messenger;
    }

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "Create new comment on user's friend post")
    @ApiResponses({
            @ApiResponse(responseCode = Constant.HTTP_STATUS_CODE_201, content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class),
                            mediaType = "application/json")})})
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable @Valid @ExistUserById Long userId,
                                           @PathVariable @Valid
                                           @ExistByPostIdAndNotDeleted Long postId,
                                           @RequestBody @Valid PostCommentCreate comment) {
        ResponseEntity<?> validationError = appValidator.validatePostCommentCreate(userDetails, userId, postId);
        if (validationError != null) {
            return validationError;
        }
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        Long newCmtId = postCommentService.createComment(loggedInUserId, postId, comment);
        messenger.broadcastComment(postId, newCmtId);
        Map<String, Long> data = new HashMap<>();
        data.put("id", newCmtId);
        BaseResponse<Map<String, Long>, ?> response =
                BaseResponse.success(HttpStatus.CREATED, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_POST_COMMENT_CREATE, data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    @Operation(summary = "Update an existing comment")
    @ApiResponses({
            @ApiResponse(responseCode = Constant.HTTP_STATUS_CODE_200, content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")})
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable @Valid @ExistUserById Long userId,
                                           @PathVariable @Valid @ExistByPostIdAndNotDeleted Long postId,
                                           @PathVariable @Valid @ExistByCommentIdAndNotDeleted Long commentId,
                                           @RequestBody @Valid PostCommentUpdate comment) {
        ResponseEntity<?> validationError = appValidator.validatePostCommentUpdate(userDetails, userId, postId, commentId);
        if (validationError != null) {
            return validationError;
        }
        Long id = postCommentService.updatePostComment(commentId, comment);
        Map<String, Long> data = new HashMap<>();
        data.put("id", id);
        BaseResponse<Map<String, Long>, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_POST_COMMENT_UPDATE, data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @Operation(summary = "Delete an existing comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema =
                    @Schema(implementation = BaseResponse.class), mediaType = "application/json")}),
    })
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> deletePostComment(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable @Valid @ExistUserById Long userId,
                                               @PathVariable @Valid @ExistByPostIdAndNotDeleted Long postId,
                                               @PathVariable @Valid @ExistByCommentIdAndNotDeleted Long commentId) {
        ResponseEntity<?> validationError = appValidator.validatePostCommentDelete(userDetails, userId, postId, commentId);
        if (validationError != null) {
            return validationError;
        }
        postCommentService.deleteCmt(commentId);
        BaseResponse<String, ?> response =
                BaseResponse.success(HttpStatus.OK, Constant.MSG_SUCCESS, Constant.MSG_SUCCESS_POST_COMMENT_DELETE, HttpStatus.NO_CONTENT.name());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
