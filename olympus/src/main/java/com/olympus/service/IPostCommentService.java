package com.olympus.service;

import com.olympus.dto.request.PostCommentCreate;
import com.olympus.dto.request.PostCommentUpdate;
import com.olympus.entity.PostComment;

public interface IPostCommentService {
    Long createComment(Long userId, Long postId, PostCommentCreate crtReq);
    Long updatePostComment(Long commentId, PostCommentUpdate updReq);
    PostComment findById(Long id);
    void deleteCmt(Long id);

    boolean existByIdAndNotDeleted(Long id);
}
