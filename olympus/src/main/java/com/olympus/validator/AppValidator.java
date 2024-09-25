package com.olympus.validator;

import com.olympus.config.Constant;
import com.olympus.dto.request.PostCreate;
import com.olympus.dto.request.PostUpdate;
import com.olympus.dto.response.BaseResponse;
import com.olympus.entity.Post;
import com.olympus.entity.PostComment;
import com.olympus.entity.Privacy;
import com.olympus.exception.InvalidImageException;
import com.olympus.exception.ReportCreateException;
import com.olympus.service.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AppValidator {
    private final IUserService userService;
    private final IPostService postService;
    private final IFriendRequestService friendRequestService;
    private final IFriendshipService friendshipService;
    private final IPostCommentService postCommentService;

    @Autowired
    public AppValidator(IUserService userService, IPostService postService, IFriendRequestService friendRequestService,
                        IFriendshipService friendshipService, IPostCommentService postCommentService) {
        this.userService = userService;
        this.postService = postService;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
        this.postCommentService = postCommentService;
    }

    public ResponseEntity<?> validatePostCreate(UserDetails userDetails, Long pathUserId, PostCreate post, MultipartFile[] files) {
        return checkCommonPostCreateAndUpdate(userDetails, pathUserId, post.getContent(), files);
    }

    public ResponseEntity<?> validatePostUpdate(UserDetails userDetails, Long pathUserId, Long pathPostId, PostUpdate post, MultipartFile[] files) {
        ResponseEntity<?> commonPostError = checkCommonPostCreateAndUpdate(userDetails, pathUserId, post.getContent(), files);
        if (commonPostError != null) {
            return commonPostError;
        }
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        Post existedPost = postService.findByPostId(pathPostId);
        Long postOwnerId = existedPost.getUser().getId();

        if (!loggedInUserId.equals(postOwnerId)) {
            String error = "Your are not post owner";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(), error);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return null;
    }

    public ResponseEntity<?> validatePostDelete(UserDetails userDetails, Long pathUserId, Long pathPostId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        Post post = postService.findByPostId(pathPostId);
        Long postOwnerId = post.getUser().getId();
        if (!loggedInUserId.equals(pathUserId)) {
            String error = "Logged in user id and target id are not the same";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(), error);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        if (!loggedInUserId.equals(postOwnerId)) {
            String error = "Your are not original poster";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(), error);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return null;
    }

    public void validateImgFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String mimeType = file.getContentType();
            boolean isImage = mimeType != null && mimeType.startsWith("image/");
            if (!isImage) {
                throw new InvalidImageException();
            }
        }
    }

    public void validateImgFile(MultipartFile[] files) {
        if (files != null) {
            for (MultipartFile file : files) {
                validateImgFile(file);
            }
        }
    }

    public ResponseEntity<?> validateFriendRequestSent(UserDetails userDetails, Long receiverId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);

        if (loggedInUserId.equals(receiverId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.BAD_REQUEST, Constant.MSG_ERROR, HttpStatus.BAD_REQUEST.name(),
                            Constant.ERR_FRIEND_REQUEST_REQUEST_DUPLICATE_SENDER_RECEIVER);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (friendshipService.existsFriendship(loggedInUserId, receiverId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.BAD_REQUEST, Constant.MSG_ERROR, HttpStatus.BAD_REQUEST.name(),
                            Constant.ERR_FRIEND_REQUEST_FRIENDSHIP_EXIST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (friendRequestService.existsByUserId(loggedInUserId, receiverId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.BAD_REQUEST, Constant.MSG_ERROR, HttpStatus.BAD_REQUEST.name(),
                            Constant.ERR_FRIEND_REQUEST_REQUEST_EXIST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<?> validateFriendRequestDelete(UserDetails userDetails, Long requestId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (!friendRequestService.isValidDeletePermission(loggedInUserId, requestId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.FORBIDDEN, Constant.MSG_ERROR, HttpStatus.FORBIDDEN.name(),
                            Constant.ERR_FRIEND_REQUEST_REQUEST_NOT_VALID_CANCELER);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public ResponseEntity<?> validateFriendRequestAccept(UserDetails userDetails, Long requestId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (!friendRequestService.validAccepter(requestId, loggedInUserId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.FORBIDDEN, Constant.MSG_ERROR, HttpStatus.FORBIDDEN.name(),
                            Constant.ERR_FRIEND_REQUEST_REQUEST_NOT_VALID_ACCEPTER);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public ResponseEntity<?> validatePostCommentCreate(UserDetails userDetails, Long pathUserId, Long pathPostId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        return validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, null);
    }

    public ResponseEntity<?> validatePostCommentUpdate(UserDetails userDetails, Long pathUserId, Long pathPostId, Long pathCommentId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        PostComment comment = postCommentService.findById(pathCommentId);
        Long commenterId = comment.getUser().getId();

        ResponseEntity<?> commonPostCommentError = validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, pathCommentId);
        if (commonPostCommentError != null) {
            return commonPostCommentError;
        }

        //Check if loggedInUserId does not match ID in comment's body
        //Only original commenter can update
        if (!loggedInUserId.equals(commenterId)) {
            String error = loggedInUserId + " is not the comment owner";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.FORBIDDEN, Constant.MSG_ERROR, HttpStatus.FORBIDDEN.name(), error);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public ResponseEntity<?> validatePostCommentDelete(UserDetails userDetails, Long pathUserId, Long pathPostId, Long pathCommentId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        PostComment comment = postCommentService.findById(pathCommentId);
        Long postOwnerId = comment.getPost().getUser().getId();
        Long commenterId = comment.getUser().getId();

        ResponseEntity<?> commonPostCommentError = validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, pathCommentId);
        if (commonPostCommentError != null) {
            return commonPostCommentError;
        }

        //Check if current user is the post owner
        if (!loggedInUserId.equals(postOwnerId) || !loggedInUserId.equals(commenterId)) {
            String error = "User with ID " + loggedInUserId + " are not the post owner or original commenter";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.FORBIDDEN, Constant.MSG_ERROR, HttpStatus.FORBIDDEN.name(), error);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public ResponseEntity<?> validateLikeOrUnlikeAction(UserDetails userDetails, Long pathUserId, Long pathPostId) {
        Long loggedUserId = userService.findIdByUserDetails(userDetails);
        return validateCommonPostComment(loggedUserId, pathUserId, pathPostId, null);
    }

    public ResponseEntity<?> validateNewsfeed(UserDetails userDetails, Long userId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (!loggedInUserId.equals(userId)) {
            String error = "Current user id: " + loggedInUserId + " and path id: " + userId + " are not the same";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(), error);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return null;
    }

    public ResponseEntity<?> validateUserUpdate(UserDetails userDetails, Long userId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (!loggedInUserId.equals(userId)) {
            String error = "Current user id: " + loggedInUserId + " and path id: " + userId + " are not the same";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(), error);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return null;
    }

    public void validateReportCreate(UserDetails userDetails, Long userId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (!loggedInUserId.equals(userId)) {
            String error = "Current user id: " + loggedInUserId + " and path id: " + userId + " are not the same";
            throw new ReportCreateException(error);
        }
    }

    public ResponseEntity<?> validateUnFriend(UserDetails userDetails, Long targetUserId) {
        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (loggedInUserId.equals(targetUserId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(),
                            Constant.ERR_FRIENDSHIP_DUPLICATE_SENDER_RECEIVER);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        if (!friendshipService.existsFriendship(loggedInUserId, targetUserId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(),
                            Constant.ERR_FRIEND_REQUEST_FRIENDSHIP_NOT_EXIST);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return null;
    }


    public ResponseEntity<?> validateGetSpecificPost(UserDetails userDetails, Long userId, Long postId) {
        Long loggedUserId = userService.findIdByUserDetails(userDetails);
        Post post = postService.findByPostId(postId);
        if (!post.getUser().getId().equals(userId)) {
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(),
                            Constant.ERR_CONFLICT_PATH_POST_ID_USER_ID);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        //Check if post is for friend only but current user is not friend
        if (post.getPrivacy().equals(Privacy.FRIENDS) && !friendshipService.existsFriendship(loggedUserId, userId)) {
            String error = "Post is for friend only";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.FORBIDDEN, Constant.MSG_ERROR, HttpStatus.FORBIDDEN.name(), error);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public ResponseEntity<?> validateCommonPostComment(Long loggedInUserId, Long pathUserId, Long pathPostId, Long pathCommentId) {
        Post post = postService.findByPostId(pathPostId);
        Long postOwnerId = post.getUser().getId();

        //Check if user ID in path variable does not match ID in the post's body
        if (!pathUserId.equals(postOwnerId)) {
            BaseResponse<String, ?> response = BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(), Constant.ERR_CONFLICT_PATH_VARIABLE_REQUEST_BODY);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        //Check if post ID in path variable does not match ID in post's comment
        if (pathCommentId != null) {
            PostComment comment = postCommentService.findById(pathCommentId);
            if (!pathPostId.equals(comment.getPost().getId())) {
                BaseResponse<String, ?> response = BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.CONFLICT.name(), Constant.ERR_CONFLICT_PATH_VARIABLE_REQUEST_BODY);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
        }

        //Check if post is private and loggedInUser is not the owner
        if (post.getPrivacy().equals(Privacy.PRIVATE)
                && !loggedInUserId.equals(postOwnerId)) {
            String error = "Post is private and " + loggedInUserId + ", " + postOwnerId + " are not same";
            BaseResponse<String, ?> response = BaseResponse.error(HttpStatus.FORBIDDEN, Constant.MSG_ERROR, HttpStatus.FORBIDDEN.name(), error);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        //Check if post is for friend only but loggedInUser is not a friend
        else if (post.getPrivacy().equals(Privacy.FRIENDS)
                && (!friendshipService.existsFriendship(loggedInUserId, postOwnerId) && !loggedInUserId.equals(postOwnerId))) {
            String error = loggedInUserId + ", " + postOwnerId + " are not friends";
            BaseResponse<String, ?> response = BaseResponse.error(HttpStatus.FORBIDDEN, Constant.MSG_ERROR, HttpStatus.FORBIDDEN.name(), error);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public ResponseEntity<?> checkCommonPostCreateAndUpdate(UserDetails userDetails, Long pathUserId, Object post, MultipartFile[] files) {
        if (files == null && post == null) {
            String error = "Post must contains at least content or image";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.BAD_REQUEST, Constant.MSG_ERROR, HttpStatus.BAD_REQUEST.name(), error);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Long loggedInUserId = userService.findIdByUserDetails(userDetails);
        if (!loggedInUserId.equals(pathUserId)) {
            String error = "Logged in user id and target id are not the same";
            BaseResponse<String, ?> response =
                    BaseResponse.error(HttpStatus.CONFLICT, Constant.MSG_ERROR, HttpStatus.BAD_REQUEST.name(), error);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return null;
    }

}
