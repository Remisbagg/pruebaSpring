package com.olympus.service;

import com.olympus.dto.response.report.WeeklyReportData;

public interface IReportService {
    WeeklyReportData generateWeeklyReport(Long userId);
}
