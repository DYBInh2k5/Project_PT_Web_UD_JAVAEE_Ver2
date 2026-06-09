package com.bookstore.dto;

import java.util.List;
import java.util.Map;

// DTO (Data Transfer Object) chứa dữ liệu thống kê doanh thu trả về cho view (template)
// Được sử dụng ở trang admin/quản lý doanh thu, render số liệu và biểu đồ Chart.js
public class RevenueDto {
    // Tổng doanh thu của ngày hiện tại (lọc theo đơn hàng có orderDate trong hôm nay)
    private Double dayRevenue;
    // Tổng doanh thu của tháng hiện tại (lọc theo tháng của orderDate)
    private Double monthRevenue;
    // Tổng doanh thu của năm hiện tại (lọc theo năm của orderDate)
    private Double yearRevenue;
    // Danh sách 7 ngày gần đây dùng cho biểu đồ cột Chart.js
    // Mỗi Map chứa "date" (String) và "revenue" (Double) tương ứng
    private List<Map<String, Object>> dailyRevenueList;

    public Double getDayRevenue() { return dayRevenue; }
    public void setDayRevenue(Double dayRevenue) { this.dayRevenue = dayRevenue; }
    public Double getMonthRevenue() { return monthRevenue; }
    public void setMonthRevenue(Double monthRevenue) { this.monthRevenue = monthRevenue; }
    public Double getYearRevenue() { return yearRevenue; }
    public void setYearRevenue(Double yearRevenue) { this.yearRevenue = yearRevenue; }
    public List<Map<String, Object>> getDailyRevenueList() { return dailyRevenueList; }
    public void setDailyRevenueList(List<Map<String, Object>> dailyRevenueList) { this.dailyRevenueList = dailyRevenueList; }
}
