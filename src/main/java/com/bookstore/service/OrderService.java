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

// Service xử lý logic nghiệp vụ đơn hàng (Order): tạo đơn, cập nhật trạng thái, thống kê doanh thu
// Phụ thuộc: OrderRepository (CSDL đơn hàng), CartService (xóa giỏ sau thanh toán), ProductService (cập nhật tồn kho)
// Các trạng thái đơn hàng: "NEW" (mới), "SHIPPED" (đang giao), "PAID" (đã thanh toán)
// Cung cấp chức năng thống kê doanh thu theo ngày, tháng, năm và 7 ngày gần nhất cho biểu đồ
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Tạo đơn hàng mới từ giỏ hàng của user (quy trình thanh toán)
    // @Transactional đảm bảo toàn bộ các thao tác (tạo đơn, trừ kho, xóa giỏ) là một giao dịch nguyên tử
    // Nếu bất kỳ bước nào thất bại, tất cả thay đổi đều được rollback
    // Thuật toán:
    // Bước 1: Tạo đối tượng Order với thông tin người nhận, ngày tạo, trạng thái mặc định "NEW"
    // Bước 2: Duyệt từng CartItem trong giỏ hàng
    //   Bước 2a: Lấy thông tin sản phẩm từ CSDL (đảm bảo dữ liệu mới nhất)
    //   Bước 2b: Kiểm tra tồn kho (stock) có đủ số lượng yêu cầu không
    //     - Nếu không đủ: ném RuntimeException, transaction rollback
    //   Bước 2c: Trừ số lượng tồn kho và cập nhật lại vào CSDL
    //   Bước 2d: Tạo OrderDetail tương ứng, tính tổng tiền
    // Bước 3: Lưu đơn hàng (cascade sẽ lưu cả danh sách OrderDetail)
    // Bước 4: Xóa toàn bộ giỏ hàng (clearCart)
    // Bước 5: Trả về đơn hàng đã tạo (kèm ID)
    @Transactional
    public Order createOrder(User user, Cart cart, String recipientName, String phone, String address) {
        Order order = new Order();
        order.setUser(user);                          // Gán user đặt hàng
        order.setOrderDate(LocalDateTime.now());       // Gán thời điểm đặt hàng hiện tại
        order.setStatus("NEW");                        // Trạng thái ban đầu: đơn hàng mới
        order.setRecipientName(recipientName);         // Tên người nhận
        order.setPhone(phone);                         // Số điện thoại liên hệ
        order.setAddress(address);                     // Địa chỉ giao hàng

        double total = 0;                              // Biến tích lũy tổng tiền đơn hàng
        List<OrderDetail> details = new ArrayList<>();  // Danh sách chi tiết đơn hàng
        // Duyệt từng sản phẩm trong giỏ hàng để tạo chi tiết đơn và kiểm tra tồn kho
        for (CartItem item : cart.getItems()) {
            Product product = productService.findById(item.getProduct().getId());  // Lấy sản phẩm mới nhất từ DB
            // Kiểm tra tồn kho: nếu không đủ hàng, ném lỗi và rollback toàn bộ transaction
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng");
            }
            // Trừ số lượng tồn kho (tránh bán quá số lượng có sẵn)
            product.setStock(product.getStock() - item.getQuantity());
            productService.save(product);              // Cập nhật lại số lượng tồn kho trong CSDL

            // Tạo chi tiết đơn hàng cho từng sản phẩm
            OrderDetail detail = new OrderDetail(order, product, item.getQuantity(), product.getPrice());
            details.add(detail);
            total += product.getPrice() * item.getQuantity();  // Cộng dồn vào tổng tiền
        }
        order.setOrderDetails(details);                // Gán danh sách chi tiết vào đơn hàng
        order.setTotalAmount(total);                   // Gán tổng tiền
        order = orderRepository.save(order);           // Lưu đơn hàng vào CSDL (cascade tự lưu OrderDetail)

        cartService.clearCart(cart);                   // Xóa toàn bộ giỏ hàng sau khi đã tạo đơn thành công
        return order;                                  // Trả về đơn hàng đã được lưu
    }

    // Lấy danh sách đơn hàng của một user cụ thể (dùng cho trang lịch sử mua hàng)
    // Sắp xếp theo ngày đặt giảm dần (đơn mới nhất hiện trước)
    // Tham số userId: ID của user, page/size: phân trang
    public Page<Order> findByUser(Integer userId, int page, int size) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId, PageRequest.of(page, size));
    }

    // Lấy tất cả đơn hàng (dành cho admin quản lý đơn hàng)
    // Sắp xếp theo ngày đặt giảm dần (đơn mới nhất hiện trước), có phân trang
    public Page<Order> findAll(int page, int size) {
        return orderRepository.findAllByOrderByOrderDateDesc(PageRequest.of(page, size));
    }

    // Tìm đơn hàng theo ID duy nhất
    // Trả về đối tượng Order nếu tìm thấy (kèm danh sách OrderDetail), null nếu không tồn tại
    // Được sử dụng để xem chi tiết đơn hàng và cập nhật trạng thái
    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Cập nhật trạng thái đơn hàng (dành cho admin)
    // Các trạng thái hợp lệ: "NEW" -> "SHIPPED" -> "PAID" (tiến trình đơn hàng)
    // Bước 1: Tìm đơn hàng theo ID
    // Bước 2: Nếu đơn hàng tồn tại -> cập nhật trạng thái và lưu lại
    // Bước 3: Nếu đơn hàng không tồn tại -> bỏ qua (không ném lỗi)
    // Edge case: có thể truyền status không hợp lệ, cần kiểm tra ở controller
    public void updateStatus(Integer orderId, String status) {
        Order order = findById(orderId);
        if (order != null) {
            order.setStatus(status);      // Gán trạng thái mới
            orderRepository.save(order);  // Lưu thay đổi vào CSDL
        }
    }

    // Tính tổng doanh thu của một ngày cụ thể
    // Bước 1: Xác định thời điểm bắt đầu ngày (00:00:00)
    // Bước 2: Xác định thời điểm kết thúc ngày (23:59:59.999999999)
    // Bước 3: Gọi Repository để tính tổng các đơn hàng trong khoảng thời gian đó
    // Xử lý các đơn hàng đã thanh toán (tùy logic trong Repository)
    public Double getRevenueByDay(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();          // 2025-01-15 00:00:00
        LocalDateTime end = date.atTime(LocalTime.MAX);     // 2025-01-15 23:59:59.999999999
        return orderRepository.getRevenueBetween(start, end);
    }

    // Tính tổng doanh thu của một tháng cụ thể
    // Bước 1: Tạo LocalDateTime từ năm, tháng, ngày đầu tháng (ngày 1, 00:00:00)
    // Bước 2: Tính thời điểm kết thúc tháng = start + 1 tháng
    // Bước 3: Gọi Repository để tính tổng doanh thu trong khoảng
    // Edge case: tháng 2 năm nhuận được xử lý tự động bởi plusMonths(1)
    public Double getRevenueByMonth(int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);  // Ngày đầu tháng, 00:00:00
        LocalDateTime end = start.plusMonths(1);                       // Ngày đầu tháng sau, 00:00:00 (khoảng [start, end))
        return orderRepository.getRevenueBetween(start, end);
    }

    // Tính tổng doanh thu của một năm cụ thể
    // Bước 1: Tạo LocalDateTime từ năm, ngày đầu năm (01/01, 00:00:00)
    // Bước 2: Tính thời điểm kết thúc năm = start + 1 năm
    // Bước 3: Gọi Repository để tính tổng doanh thu trong khoảng
    public Double getRevenueByYear(int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);  // Ngày đầu năm, 00:00:00
        LocalDateTime end = start.plusYears(1);                    // Ngày đầu năm sau, 00:00:00
        return orderRepository.getRevenueBetween(start, end);
    }

    // Lấy doanh thu 7 ngày gần nhất (dùng cho biểu đồ cột ở trang admin)
    // Thuật toán:
    // Bước 1: Xác định khoảng thời gian 7 ngày (từ 6 ngày trước đến cuối ngày hiện tại)
    // Bước 2: Gọi Repository lấy dữ liệu doanh thu gộp theo ngày từ CSDL (dùng GROUP BY)
    // Bước 3: Chuyển kết quả từ List<Object[]> sang Map<LocalDate, Double> để dễ tra cứu
    //   - Mỗi Object[] chứa: [0] = java.sql.Date (ngày), [1] = Number (tổng doanh thu)
    // Bước 4: Duyệt từ i=6 đến i=0 (từ ngày cũ nhất đến ngày hiện tại), tạo Map chứa:
    //   - day: ngày trong tháng, month: tháng, year: năm
    //   - label: chuỗi "day/month" dùng làm nhãn trên biểu đồ
    //   - revenue: doanh thu ngày đó (0.0 nếu không có đơn hàng)
    // Edge case: ngày không có đơn hàng -> revenue = 0.0 nhờ getOrDefault
    // Edge case: ngày hiện tại chưa kết thúc -> chỉ tính doanh thu đến thời điểm hiện tại
    public List<Map<String, Object>> getDailyRevenue7Days(LocalDate date) {
        // Xác định khoảng thời gian 7 ngày
        LocalDateTime start = date.minusDays(6).atStartOfDay();  // 00:00:00 của 6 ngày trước
        LocalDateTime end = date.atTime(LocalTime.MAX);          // 23:59:59 của ngày hiện tại
        List<Object[]> results = orderRepository.getDailyRevenue(start, end);  // Lấy doanh thu gộp theo ngày từ DB

        // Chuyển đổi kết quả từ Object[] sang Map để tra cứu nhanh theo ngày
        Map<LocalDate, Double> revenueMap = new HashMap<>();
        // Duyệt từng dòng kết quả: mỗi dòng gồm (ngày, tổng doanh thu)
        for (Object[] row : results) {
            java.sql.Date sqlDate = (java.sql.Date) row[0];    // Chuyển cột ngày từ SQL sang Java Date
            LocalDate d = sqlDate.toLocalDate();                // Chuyển sang LocalDate
            Double total = ((Number) row[1]).doubleValue();     // Chuyển tổng doanh thu sang Double (xử lý cả BigInteger, BigDecimal, etc.)
            revenueMap.put(d, total);                           // Lưu vào Map: key = ngày, value = doanh thu
        }

        // Tạo danh sách 7 ngày (từ 6 ngày trước đến ngày hiện tại)
        List<Map<String, Object>> dailyList = new ArrayList<>();
        // Duyệt từ i = 6 (ngày cũ nhất) đến i = 0 (ngày hiện tại)
        for (int i = 6; i >= 0; i--) {
            LocalDate d = date.minusDays(i);                    // Tính ngày tương ứng
            Map<String, Object> item = new HashMap<>();
            item.put("day", d.getDayOfMonth());                 // Ngày trong tháng (1-31)
            item.put("month", d.getMonthValue());               // Tháng (1-12)
            item.put("year", d.getYear());                      // Năm
            item.put("label", d.getDayOfMonth() + "/" + d.getMonthValue());  // Nhãn hiển thị trên biểu đồ (VD: "15/1")
            item.put("revenue", revenueMap.getOrDefault(d, 0.0));  // Doanh thu ngày đó (0 nếu không có dữ liệu)
            dailyList.add(item);                                // Thêm vào danh sách kết quả
        }
        return dailyList;                                       // Trả về danh sách 7 ngày đã được sắp xếp từ cũ đến mới
    }
}
