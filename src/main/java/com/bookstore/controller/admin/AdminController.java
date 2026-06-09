package com.bookstore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Controller admin: trang dashboard tổng quan (yêu cầu đăng nhập + quyền ADMIN)
// Base URL: /admin - Spring Security được cấu hình để yêu cầu role ADMIN cho tất cả URL /admin/**
// Không phụ thuộc service nào (trang dashboard tĩnh, không có dữ liệu động)
@Controller
@RequestMapping("/admin")
public class AdminController {

    // Xử lý GET /admin - Trang tổng quan dành cho quản trị viên
    // Không xử lý tham số, không tương tác database
    // Trả về template "admin/dashboard" (file dashboard.html trong thư mục admin/)
    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }
}
