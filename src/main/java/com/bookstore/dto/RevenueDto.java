package com.bookstore.dto;

import java.util.List;
import java.util.Map;

public class RevenueDto {
    private Double dayRevenue;
    private Double monthRevenue;
    private Double yearRevenue;
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
