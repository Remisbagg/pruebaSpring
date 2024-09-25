package com.back.service;

import com.back.dto.response.report.WeeklyReportData;

public interface IReportService {
    WeeklyReportData generateWeeklyReport(Long userId);
}
