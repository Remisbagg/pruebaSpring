package com.olympus.service.impl;

import com.olympus.dto.response.report.WeeklyReportData;
import com.olympus.repository.IFriendshipRepository;
import com.olympus.repository.IPostCommentRepository;
import com.olympus.repository.IPostLikeRepository;
import com.olympus.repository.IPostRepository;
import com.olympus.service.IReportService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReportServiceImpl implements IReportService {
    private final IPostRepository postRepository;
    private final IPostLikeRepository postLikeRepository;
    private final IPostCommentRepository postCommentRepository;
    private final IFriendshipRepository friendshipRepository;

    @Autowired
    public ReportServiceImpl(IPostRepository postRepository,
                             IPostLikeRepository postLikeRepository,
                             IPostCommentRepository postCommentRepository,
                             IFriendshipRepository friendshipRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public WeeklyReportData generateWeeklyReport(Long userId) {
        LocalDateTime lastWeekTime = LocalDateTime.now().minusWeeks(1);
        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        List<Long> listPostIds = postRepository.findListPostIdsOfUser(userId);

        int newPosts = postRepository.countTotalPostLastWeek(userId, lastWeekTime);
        int newFriends = friendshipRepository.countTotalNewFriendsLastWeeks(userId, lastWeek);
        int newLikes = (listPostIds == null) ? 0 : postLikeRepository.countNewPostLikesLastWeek(listPostIds, lastWeekTime);
        int newComments = (listPostIds == null) ? 0 : postCommentRepository.countNewPostCommentLastWeek(listPostIds, lastWeekTime);
        return new WeeklyReportData(newPosts, newFriends, newLikes, newComments);
    }
}
