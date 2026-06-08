package com.bookstore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Controller admin: trang dashboard (yêu cầu đăng nhập + quyền ADMIN)
@Controller
@RequestMapping("/admin")
public class AdminController {

    // Trang tổng quan admin
    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }
}
