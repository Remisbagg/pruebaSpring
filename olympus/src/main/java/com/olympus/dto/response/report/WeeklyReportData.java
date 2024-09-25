package com.olympus.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyReportData {
    private int newPostsLastWeek;
    private int newFriendsLastWeek;
    private int newLikesLastWeek;
    private int newCommentsLastWeek;
}
