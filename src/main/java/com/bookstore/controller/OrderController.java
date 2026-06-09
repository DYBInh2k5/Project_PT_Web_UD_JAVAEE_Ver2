package com.bookstore.controller;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controller xem lịch sử và chi tiết đơn hàng (dành cho khách hàng đã đăng nhập)
// Base URL: /order - Yêu cầu người dùng phải đăng nhập (Authentication object được inject)
// Phụ thuộc: OrderService (truy vấn đơn hàng), UserService (lấy thông tin user từ authentication)
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Helper: Lấy thông tin User đang đăng nhập từ Spring Security Authentication
    // Authentication.getName() trả về username đã xác thực -> truy vấn User từ database
    // Nếu auth null hoặc chưa xác thực -> trả về null (chưa đăng nhập)
    private User getLoggedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByUsername(auth.getName());
    }

    // Xử lý GET /order/history - Lịch sử đơn hàng của khách hàng (phân trang, 10 đơn/trang)
    // Tham số: page (số trang, mặc định 0)
    // B1: Lấy user đăng nhập, nếu null redirect /login
    // B2: Gọi OrderService.findByUser(userId, page, 10) để lấy trang đơn hàng
    // B3: Đưa danh sách đơn hàng + thông tin phân trang vào Model
    // B4: Trả về template "order-history"
    @GetMapping("/history")
    public String orderHistory(Model model,
                               Authentication auth,
                               @RequestParam(defaultValue = "0") int page) {
        User user = getLoggedUser(auth);
        if (user == null) return "redirect:/login";
        Page<Order> orders = orderService.findByUser(user.getId(), page, 10);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        return "order-history";
    }

    // Xử lý GET /order/detail/{id} - Xem chi tiết một đơn hàng (chỉ chủ sở hữu mới xem được)
    // Tham số: id (ID của đơn hàng từ URL path)
    // B1: Lấy user đăng nhập, nếu null redirect /login
    // B2: Tìm Order theo id, nếu null hoặc không thuộc về user hiện tại -> redirect /order/history
    //     (Kiểm tra order.getUser().getId().equals(user.getId()) để đảm bảo chỉ chủ sở hữu xem được)
    // B3: Đưa đối tượng order vào Model (kèm danh sách OrderItem)
    // B4: Trả về template "order-detail"
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Integer id, Model model, Authentication auth) {
        User user = getLoggedUser(auth);
        if (user == null) return "redirect:/login";
        Order order = orderService.findById(id);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/order/history";
        }
        model.addAttribute("order", order);
        return "order-detail";
    }
}
