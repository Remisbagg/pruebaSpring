package com.olympus.service.impl;

import com.olympus.dto.response.report.WeeklyReportData;
import com.olympus.repository.IFriendshipRepository;
import com.olympus.repository.IPostCommentRepository;
import com.olympus.repository.IPostLikeRepository;
import com.olympus.repository.IPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
    @InjectMocks
    private ReportServiceImpl reportService;
    @Mock
    private IPostRepository postRepository;
    @Mock
    private IPostLikeRepository postLikeRepository;
    @Mock
    private IPostCommentRepository postCommentRepository;
    @Mock
    private IFriendshipRepository friendshipRepository;

    @Test
    public void testGenerateWeeklyReport() {
        // Arrange
        Long userId = 1L;
        int expectedNewPosts = 5;
        int expectedNewFriends = 1;
        int expectedNewLikes = 12;
        int expectedNewComments = 7;

        when(postRepository.countTotalPostLastWeek(eq(userId), any(LocalDateTime.class))).thenReturn(expectedNewPosts);
        when(friendshipRepository.countTotalNewFriendsLastWeeks(eq(userId), any(LocalDate.class))).thenReturn(expectedNewFriends);

        // Using argument matchers to cover both empty and null lists
        when(postLikeRepository.countNewPostLikesLastWeek(anyList(), any(LocalDateTime.class))).thenReturn(expectedNewLikes);
        when(postCommentRepository.countNewPostCommentLastWeek(anyList(), any(LocalDateTime.class))).thenReturn(expectedNewComments);

        // Act
        WeeklyReportData reportData = reportService.generateWeeklyReport(userId);

        // Assert
        assertNotNull(reportData);
        assertEquals(expectedNewPosts, reportData.getNewPostsLastWeek());
        assertEquals(expectedNewFriends, reportData.getNewFriendsLastWeek());
        assertEquals(expectedNewLikes, reportData.getNewLikesLastWeek());
        assertEquals(expectedNewComments, reportData.getNewCommentsLastWeek());
    }

}