package com.back.service.impl;

import com.back.dto.request.PostCommentCreate;
import com.back.dto.request.PostCommentUpdate;
import com.back.entity.Post;
import com.back.entity.PostComment;
import com.back.entity.User;
import com.back.mapper.PostCommentCreateMapper;
import com.back.mapper.PostCommentUpdateMapper;
import com.back.repository.IPostCommentRepository;
import com.back.service.IPostCommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PostCommentServiceImpl implements IPostCommentService {
    private final IPostCommentRepository commentRepository;
    private final PostCommentCreateMapper commentCreateMapper;
    private final PostCommentUpdateMapper cmtUpdMapper;

    @Autowired
    public PostCommentServiceImpl(IPostCommentRepository commentRepository,
                                  PostCommentCreateMapper commentCreateMapper,
                                  PostCommentUpdateMapper cmtUpdMapper) {
        this.commentRepository = commentRepository;
        this.commentCreateMapper = commentCreateMapper;
        this.cmtUpdMapper = cmtUpdMapper;
    }

    @Override
    public Long createComment(Long userId, Long postId, PostCommentCreate crtReq) {
        PostComment postComment = commentCreateMapper.dtoToEntity(crtReq);
        postComment.setPost(new Post(postId));
        postComment.setUser(new User(userId));
        return commentRepository.save(postComment).getId();
    }

    @Override
    public Long updatePostComment(Long commentId, PostCommentUpdate updReq) {
        PostComment currentComment = commentRepository.getReferenceById(commentId);
        cmtUpdMapper.updateEntityFromDTO(updReq, currentComment);
        return commentId;
    }

    @Override
    public PostComment findById(Long id) {
        return commentRepository.getReferenceById(id);
    }

    @Override
    public void deleteCmt(Long id) {
        PostComment comment = commentRepository.getReferenceById(id);
        comment.setDeleteStatus(true);
        commentRepository.save(comment);
    }

    @Override
    public boolean existByIdAndNotDeleted(Long id) {
        return commentRepository.existByIdAndNotDeleted(id) == 1;
    }
}
