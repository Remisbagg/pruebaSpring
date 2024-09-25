package com.back.service;

import com.back.dto.request.PostCommentCreate;
import com.back.dto.request.PostCommentUpdate;
import com.back.model.PostComment;

public interface IPostCommentService {
    Long createComment(Long userId, Long postId, PostCommentCreate crtReq);
    Long updatePostComment(Long commentId, PostCommentUpdate updReq);
    PostComment findById(Long id);
    void deleteCmt(Long id);

    boolean existByIdAndNotDeleted(Long id);
}
