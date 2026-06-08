package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

// Xử lý logic đơn hàng: tạo đơn, cập nhật trạng thái, thống kê doanh thu
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Tạo đơn hàng từ giỏ hàng (kiểm tra tồn kho, trừ hàng, xóa giỏ)
    @Transactional
    public Order createOrder(User user, Cart cart, String recipientName, String phone, String address) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        order.setRecipientName(recipientName);
        order.setPhone(phone);
        order.setAddress(address);

        double total = 0;
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product product = productService.findById(item.getProduct().getId());
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng");
            }
            product.setStock(product.getStock() - item.getQuantity());
            productService.save(product);

            OrderDetail detail = new OrderDetail(order, product, item.getQuantity(), product.getPrice());
            details.add(detail);
            total += product.getPrice() * item.getQuantity();
        }
        order.setOrderDetails(details);
        order.setTotalAmount(total);
        order = orderRepository.save(order);

        cartService.clearCart(cart);   // Xóa giỏ hàng sau khi thanh toán
        return order;
    }

    // Lấy đơn hàng của 1 user (phân trang)
    public Page<Order> findByUser(Integer userId, int page, int size) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId, PageRequest.of(page, size));
    }

    // Lấy tất cả đơn hàng (admin, phân trang)
    public Page<Order> findAll(int page, int size) {
        return orderRepository.findAllByOrderByOrderDateDesc(PageRequest.of(page, size));
    }

    // Tìm đơn hàng theo ID
    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Cập nhật trạng thái đơn hàng (NEW, SHIPPED, PAID)
    public void updateStatus(Integer orderId, String status) {
        Order order = findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    // Tính tổng doanh thu của 1 ngày
    public Double getRevenueByDay(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return orderRepository.getRevenueBetween(start, end);
    }

    // Tính tổng doanh thu của 1 tháng
    public Double getRevenueByMonth(int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        return orderRepository.getRevenueBetween(start, end);
    }

    // Tính tổng doanh thu của 1 năm
    public Double getRevenueByYear(int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = start.plusYears(1);
        return orderRepository.getRevenueBetween(start, end);
    }

    // Lấy doanh thu 7 ngày gần nhất (dùng cho biểu đồ cột)
    public List<Map<String, Object>> getDailyRevenue7Days(LocalDate date) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<Object[]> results = orderRepository.getDailyRevenue(start, end);

        Map<LocalDate, Double> revenueMap = new HashMap<>();
        for (Object[] row : results) {
            java.sql.Date sqlDate = (java.sql.Date) row[0];
            LocalDate d = sqlDate.toLocalDate();
            Double total = ((Number) row[1]).doubleValue();
            revenueMap.put(d, total);
        }

        // Tạo list 7 ngày (từ 6 ngày trước đến ngày hiện tại)
        List<Map<String, Object>> dailyList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = date.minusDays(i);
            Map<String, Object> item = new HashMap<>();
            item.put("day", d.getDayOfMonth());
            item.put("month", d.getMonthValue());
            item.put("year", d.getYear());
            item.put("label", d.getDayOfMonth() + "/" + d.getMonthValue());
            item.put("revenue", revenueMap.getOrDefault(d, 0.0));
            dailyList.add(item);
        }
        return dailyList;
    }
}
