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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/orders")
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page) {
        Page<Order> orders = orderService.findAll(page, 10);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        return "admin/orders";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Integer orderId,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        orderService.updateStatus(orderId, status);
        ra.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công");
        return "redirect:/admin/orders";
    }

    @GetMapping("/revenue")
    public String revenueForm(Model model) {
        return "admin/revenue";
    }

    @PostMapping("/revenue")
    public String revenue(@RequestParam String date, Model model) {
        LocalDate selectedDate = LocalDate.parse(date);
        RevenueDto dto = new RevenueDto();
        dto.setDayRevenue(orderService.getRevenueByDay(selectedDate));
        dto.setMonthRevenue(orderService.getRevenueByMonth(selectedDate.getYear(), selectedDate.getMonthValue()));
        dto.setYearRevenue(orderService.getRevenueByYear(selectedDate.getYear()));
        dto.setDailyRevenueList(orderService.getDailyRevenue7Days(selectedDate));

        model.addAttribute("revenue", dto);
        model.addAttribute("selectedDate", date);
        return "admin/revenue";
    }
}
