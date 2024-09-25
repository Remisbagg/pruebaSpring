package com.olympus.controller;

import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.dto.response.report.WeeklyReportData;
import com.olympus.service.IReportService;
import com.olympus.service.IUserService;
import com.olympus.validator.AppValidator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@Import(SecurityConfig.class)
class ReportControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AppValidator appValidator;
    @MockBean
    private IReportService reportService;
    @MockBean
    private IUserService userService;

    @Test
    @WithMockUser
    void testGenerateWeeklyReport_Success() throws Exception {
        //Arrange
        Long userId = 1L;
        WeeklyReportData report = mock(WeeklyReportData.class);
        Workbook mockWorkbook = new HSSFWorkbook(); // or use XSSFWorkbook for .xlsx

        doNothing().when(appValidator).validateReportCreate(any(UserDetails.class), any());
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(reportService.generateWeeklyReport(userId)).thenReturn(report);

        // Act & Assert
        mockMvc.perform(get("/v1/{userId}/report/weekly", userId)) // Replace with your actual endpoint
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.ms-excel"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=report.xlsx"));
    }

}