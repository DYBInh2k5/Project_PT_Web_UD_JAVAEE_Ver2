package com.bookstore.controller;

import com.bookstore.dto.RegisterDto;
import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// Controller xử lý đăng nhập, đăng ký tài khoản (public, không yêu cầu đăng nhập)
// Xử lý các URL: GET /login, GET /register, POST /register
// (POST /login do Spring Security Filter tự động xử lý, không cần controller method)
// Phụ thuộc: UserService (xử lý nghiệp vụ đăng ký tài khoản)
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Xử lý GET /login - Hiển thị form đăng nhập
    // Spring Security tự động xử lý POST /login (xác thực username/password)
    // Nếu đăng nhập thành công, redirect về trang mặc định (thường là /)
    // Nếu thất bại, quay lại /login?error
    // Trả về template "login"
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Xử lý GET /register - Hiển thị form đăng ký tài khoản mới
    // B1: Khởi tạo một RegisterDto rỗng để binding dữ liệu từ form
    // B2: Đưa đối tượng user vào Model với key "user" để Thymeleaf binding với th:object="${user}"
    // Trả về template "register"
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new RegisterDto());
        return "register";
    }

    // Xử lý POST /register - Xử lý đăng ký tài khoản mới
    // Tham số: user (RegisterDto được binding từ form, có @Valid để kiểm tra validation),
    //          result (BindingResult chứa lỗi validation nếu có)
    // B1: Kiểm tra nếu có lỗi validation (result.hasErrors()) -> quay lại form đăng ký
    // B2: Gọi UserService.register(dto) để tạo tài khoản mới
    // B3: Nếu thành công -> redirect sang /login?success=true (thông báo đăng ký thành công)
    // B4: Nếu UserService ném RuntimeException (vd: email/username đã tồn tại) ->
    //     đưa thông báo lỗi vào Model và quay lại form đăng ký
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterDto dto,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.register(dto);
            return "redirect:/login?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
