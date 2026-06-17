package com.bookstore.controller.admin;

import com.bookstore.dto.RevenueDto;
import com.bookstore.entity.Order;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Controller admin: quản lý đơn hàng và xem doanh thu - yêu cầu quyền ADMIN
// Base URL: /admin/orders - Spring Security chặn tất cả URL /admin/** yêu cầu role ADMIN
// Phụ thuộc: OrderService (truy vấn đơn hàng, cập nhật trạng thái, tính doanh thu)
@Controller
@RequestMapping("/admin/orders")
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    // Xử lý GET /admin/orders - Danh sách đơn hàng (phân trang, sắp xếp mới nhất trước)
    // Tham số: page (số trang, mặc định 0)
    // B1: Gọi OrderService.findAll(page, 10) để lấy trang đơn hàng (10 đơn/trang)
    // B2: Đưa danh sách đơn hàng và thông tin phân trang vào Model
    // B3: Trả về template "admin/orders"
    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page) {
        Page<Order> orders = orderService.findAll(page, 10);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        return "admin/orders";
    }

    // Xử lý POST /admin/orders/update-status - Cập nhật trạng thái đơn hàng
    // Tham số: orderId (ID đơn hàng cần cập nhật), status (trạng thái mới: NEW, SHIPPED, PAID)
    // B1: Gọi OrderService.updateStatus(orderId, status) để cập nhật trạng thái trong database
    // B2: Flash success + redirect /admin/orders (quay lại danh sách)
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Integer orderId,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        orderService.updateStatus(orderId, status);
        ra.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công");
        return "redirect:/admin/orders";
    }

    // Xử lý GET /admin/orders/revenue - Hiển thị form xem doanh thu (chưa có dữ liệu)
    // Không xử lý tham số, chỉ trả về template trống để người dùng nhập ngày
    // Trả về template "admin/revenue"
    @GetMapping("/revenue")
    public String revenueForm(Model model) {
        return "admin/revenue";
    }

    // Xử lý POST /admin/orders/revenue - Xử lý xem doanh thu theo ngày được chọn
    // Tham số: date (chuỗi ngày tháng từ form, định dạng yyyy-MM-dd)
    // B1: Parse chuỗi date thành LocalDate
    // B2: Khởi tạo RevenueDto để chứa tất cả dữ liệu doanh thu
    // B3: Gọi OrderService:
    //     - getRevenueByDay(selectedDate): tổng doanh thu trong ngày được chọn
    //     - getRevenueByMonth(year, month): tổng doanh thu trong tháng của ngày đó
    //     - getRevenueByYear(year): tổng doanh thu trong năm của ngày đó
    //     - getDailyRevenue7Days(selectedDate): danh sách doanh thu 7 ngày gần nhất (cho biểu đồ)
    // B4: Đưa dto và selectedDate vào Model
    // B5: Trả về template "admin/revenue" (lần này đã có dữ liệu để hiển thị)
    @PostMapping("/revenue")
    public String revenue(@RequestParam String date, Model model) {
        LocalDate selectedDate = LocalDate.parse(date);
        RevenueDto dto = new RevenueDto();
        dto.setDayRevenue(orderService.getRevenueByDay(selectedDate));
        dto.setMonthRevenue(orderService.getRevenueByMonth(selectedDate.getYear(), selectedDate.getMonthValue()));
        dto.setYearRevenue(orderService.getRevenueByYear(selectedDate.getYear()));
        dto.setDailyRevenueList(orderService.getDailyRevenue7Days(selectedDate));

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = dto.getDailyRevenueList();
        try {
            model.addAttribute("labelsJson", mapper.writeValueAsString(
                list.stream().map(m -> m.get("label")).collect(Collectors.toList())));
            model.addAttribute("valuesJson", mapper.writeValueAsString(
                list.stream().map(m -> m.get("revenue")).collect(Collectors.toList())));
        } catch (JsonProcessingException e) {
            model.addAttribute("labelsJson", "[]");
            model.addAttribute("valuesJson", "[]");
        }

        model.addAttribute("revenue", dto);
        model.addAttribute("selectedDate", date);
        return "admin/revenue";
    }
}
