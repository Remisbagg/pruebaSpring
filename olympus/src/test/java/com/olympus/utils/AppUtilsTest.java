package com.olympus.utils;

import com.olympus.dto.response.report.WeeklyReportData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AppUtilsTest {
    @Test
    void whenGeneratingReport_thenCorrectFormat() {
        // Arrange
        WeeklyReportData mockData = new WeeklyReportData();
        mockData.setNewPostsLastWeek(10);
        mockData.setNewFriendsLastWeek(5);
        mockData.setNewLikesLastWeek(20);
        mockData.setNewCommentsLastWeek(3);

        // Act
        Workbook workbook = AppUtils.generateWeeklyReport(mockData);

        // Assert
        assertNotNull(workbook);
        assertEquals(1, workbook.getNumberOfSheets());
        Sheet sheet = workbook.getSheet("Weekly Report");
        assertNotNull(sheet);

        // Check headers
        Row headerRow = sheet.getRow(0);
        assertNotNull(headerRow);
        assertEquals("New Post", headerRow.getCell(0).getStringCellValue());
        assertEquals("New Friends", headerRow.getCell(1).getStringCellValue());
        assertEquals("New Likes", headerRow.getCell(2).getStringCellValue());
        assertEquals("New Comments", headerRow.getCell(3).getStringCellValue());

        // Check data
        Row dataRow = sheet.getRow(1);
        assertNotNull(dataRow);
        assertEquals(mockData.getNewPostsLastWeek(), (int) dataRow.getCell(0).getNumericCellValue());
        assertEquals(mockData.getNewFriendsLastWeek(), (int) dataRow.getCell(1).getNumericCellValue());
        assertEquals(mockData.getNewLikesLastWeek(), (int) dataRow.getCell(2).getNumericCellValue());
        assertEquals(mockData.getNewCommentsLastWeek(), (int) dataRow.getCell(3).getNumericCellValue());

        // Close workbook to avoid memory leaks - in real-world scenarios, ensure proper closing of resources
        try {
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}