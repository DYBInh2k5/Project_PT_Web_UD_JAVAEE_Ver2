package com.bookstore.dto;

import java.util.List;
import java.util.Map;

// DTO chứa dữ liệu doanh thu trả về cho view (ngày, tháng, năm + biểu đồ)
public class RevenueDto {
    private Double dayRevenue;               // Tổng doanh thu ngày
    private Double monthRevenue;             // Tổng doanh thu tháng
    private Double yearRevenue;              // Tổng doanh thu năm
    private List<Map<String, Object>> dailyRevenueList;  // 7 ngày cho biểu đồ cột

    public Double getDayRevenue() { return dayRevenue; }
    public void setDayRevenue(Double dayRevenue) { this.dayRevenue = dayRevenue; }
    public Double getMonthRevenue() { return monthRevenue; }
    public void setMonthRevenue(Double monthRevenue) { this.monthRevenue = monthRevenue; }
    public Double getYearRevenue() { return yearRevenue; }
    public void setYearRevenue(Double yearRevenue) { this.yearRevenue = yearRevenue; }
    public List<Map<String, Object>> getDailyRevenueList() { return dailyRevenueList; }
    public void setDailyRevenueList(List<Map<String, Object>> dailyRevenueList) { this.dailyRevenueList = dailyRevenueList; }
}
