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

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    private User getLoggedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByUsername(auth.getName());
    }

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
