package com.olympus.service.impl;

import com.olympus.dto.request.PostCreate;
import com.olympus.dto.request.PostUpdate;
import com.olympus.dto.response.OtherUserPost;
import com.olympus.dto.response.curentuserpost.CurrentUserPost;
import com.olympus.dto.response.newsfeed.NewsfeedPostDTO;
import com.olympus.entity.Post;
import com.olympus.entity.PostComment;
import com.olympus.entity.Privacy;
import com.olympus.entity.User;
import com.olympus.mapper.*;
import com.olympus.repository.IPostRepository;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IPostImageService;
import com.olympus.service.IPostService;
import com.olympus.utils.CustomPage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements IPostService {
    private final IPostRepository postRepository;
    private final PostCreateMapper postCreateMapper;
    private final IPostImageService postImageService;
    private final PostUpdateMapper postUpdateMapper;
    private final CurrentUserPostMapper currentUserPostMapper;
    private final IFriendshipService friendshipService;
    private final NewsfeedPostMapper newsfeedPostMapper;
    private final OtherUserPostMapper otherUserPostMapper;

    @Autowired
    public PostServiceImpl(IPostRepository postRepository,
                           PostCreateMapper postCreateMapper,
                           IPostImageService postImageService,
                           PostUpdateMapper postUpdateMapper,
                           CurrentUserPostMapper currentUserPostMapper,
                           IFriendshipService friendshipService,
                           NewsfeedPostMapper newsfeedPostMapper,
                           OtherUserPostMapper otherUserPostMapper) {
        this.postRepository = postRepository;
        this.postCreateMapper = postCreateMapper;
        this.postImageService = postImageService;
        this.postUpdateMapper = postUpdateMapper;
        this.currentUserPostMapper = currentUserPostMapper;
        this.friendshipService = friendshipService;
        this.newsfeedPostMapper = newsfeedPostMapper;
        this.otherUserPostMapper = otherUserPostMapper;
    }

    @Override
    public Long createPost(Long userId, PostCreate postCreate, List<String> imageUrls) {
        Post newPost = postCreateMapper.toPost(postCreate);
        newPost.setUser(new User(userId));
        Long newPostId = postRepository.save(newPost).getId();

        if (!imageUrls.isEmpty()) {
            postImageService.save(imageUrls, newPost);
        }

        return newPostId;
    }

    @Override
    public Long updatePost(Long postId, PostUpdate postUpdate, List<String> imageUrls) {
        Post post = postRepository.findById(postId).orElseThrow();
        postUpdateMapper.updateEntity(postUpdate, post);

        postImageService.deleteByPost(post);

        if (!imageUrls.isEmpty()) {
            postImageService.save(imageUrls, post);
        }
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public boolean existByPostIdAndNotDeleted(Long id) {
        return postRepository.existByIdAndNotDeleted(id) == 1;
    }

    @Override
    public boolean existsByIdAndUserId(Long postId, Long userId) {
        return postRepository.existsByIdAndUser_Id(postId, userId);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.getReferenceById(id);
        post.setDeleteStatus(true);
        postRepository.save(post);
    }

    @Override
    public Post findByPostId(long id) {
        return postRepository.getReferenceById(id);
    }

    @Override
    public Page<NewsfeedPostDTO> getNewsfeed(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        List<Long> friendIds = friendshipService.getListFriendIds(userId);
        if (friendIds == null || friendIds.isEmpty()) {
            return Page.empty(pageable);
        }
        Page<Post> postsPage = postRepository.findPostByFriendsAndDeleteStatusAndPrivacy(friendIds, pageable);
        List<NewsfeedPostDTO> newsfeed = newsfeedPostMapper.toListDTO(postsPage.getContent());
        return new PageImpl<>(newsfeed, pageable, postsPage.getTotalElements());
    }

    @Override
    public CustomPage<NewsfeedPostDTO> getNewsfeedWithCustomPage(Long userId, int page, int size) {
        return new CustomPage<>(getNewsfeed(userId, page, size));
    }

    @Override
    public Page<CurrentUserPost> getCurrentUserPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.getCurrentUserPosts(userId, pageable);
        filterDeletedComments(posts);
        List<CurrentUserPost> currentUserPosts = currentUserPostMapper.toListDTOs(posts.getContent());
        return new PageImpl<>(currentUserPosts, pageable, posts.getTotalElements());
    }

    @Override
    public CustomPage<CurrentUserPost> getCurrentUserPostsWithCustomPage(Long loggedInUserId, int page, int size) {
        return new CustomPage<>(getCurrentUserPosts(loggedInUserId, page, size));
    }

    @Override
    public Page<OtherUserPost> getFriendPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<Post> posts = postRepository.findFriendUserPost(userId, pageable);
        filterDeletedComments(posts);
        List<OtherUserPost> friendUserPosts = otherUserPostMapper.toListDTOs(posts.getContent());
        return new PageImpl<>(friendUserPosts, pageable, posts.getTotalElements());
    }

    @Override
    public CustomPage<OtherUserPost> getFriendPostsWithCustomPage(Long userId, int page, int size) {
        return new CustomPage<>(getFriendPosts(userId, page, size));
    }

    @Override
    public Page<OtherUserPost> getOtherUserPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findOtherUserPost(userId, pageable);
        filterDeletedComments(posts);
        List<OtherUserPost> friendUserPosts = otherUserPostMapper.toListDTOs(posts.getContent());
        return new PageImpl<>(friendUserPosts, pageable, posts.getTotalElements());
    }

    @Override
    public CustomPage<OtherUserPost> getOtherUserPostsWithCustomPage(Long userId, int page, int size) {
        return new CustomPage<>(getOtherUserPosts(userId, page, size));
    }

    @Override
    public CurrentUserPost getCurrentUserSpecificPost(Long userId, Long postId) {
        Post post = postRepository.getSpecificPost(userId, postId);
        return currentUserPostMapper.toDTO(post);
    }

    @Override
    public OtherUserPost getFriendSpecificPost(Long userId, Long postId) {
        Post post = postRepository.getSpecificPost(userId, postId);
        return otherUserPostMapper.toDTO(post);
    }

    @Override
    public OtherUserPost getOtherUserSpecificPost(Long userId, Long postId) {
        Post post = postRepository.getSpecificPost(userId, postId);
        return otherUserPostMapper.toDTO(post);
    }

    @Override
    public NewsfeedPostDTO getSingleNewsfeedPost(Long postId) {
        Post post = postRepository.getPostsById(postId);
        if(post.getPrivacy().equals(Privacy.FRIENDS) || post.getPrivacy().equals(Privacy.PUBLIC)) {
            return newsfeedPostMapper.toDTO(post);
        }
        return null;
    }

    private static void filterDeletedComments(Page<Post> posts) {
        for (Post p : posts) {
            List<PostComment> comments = p.getComments().stream().filter(c -> !c.isDeleteStatus())
                    .toList();
            p.setComments(comments);
        }
    }
}
