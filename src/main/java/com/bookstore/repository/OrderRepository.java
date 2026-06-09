package com.bookstore.repository;

import com.bookstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
// Repository quản lý truy vấn dữ liệu đơn hàng (Order)
// Cung cấp các phương thức tra cứu đơn hàng cho người dùng và admin
// Hỗ trợ thống kê doanh thu với các truy vấn tổng hợp và nhóm theo ngày
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Lấy danh sách đơn hàng của một người dùng, sắp xếp theo ngày đặt giảm dần (mới nhất trước)
    // Dùng trên trang lịch sử mua hàng của người dùng
    // Tham số: userId - ID của người dùng, pageable - thông tin trang và sắp xếp
    // JPQL sinh tự động: WHERE o.user.id = :userId ORDER BY o.orderDate DESC
    Page<Order> findByUserIdOrderByOrderDateDesc(Integer userId, Pageable pageable);
    // Lấy tất cả đơn hàng trong hệ thống, sắp xếp mới nhất trước
    // Dùng trên trang quản lý đơn hàng (admin) để xem toàn bộ giao dịch
    // Tham số: pageable - thông tin trang và sắp xếp
    // JPQL sinh tự động: ORDER BY o.orderDate DESC
    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
    // Tính tổng doanh thu trong một khoảng thời gian nhất định
    // Dùng trên trang thống kê dashboard (admin) để hiển thị tổng doanh thu theo tháng/tuần
    // Logic JPQL:
    //   - SELECT COALESCE(SUM(o.totalAmount), 0): tính tổng cột totalAmount, trả về 0 nếu không có đơn hàng
    //   - WHERE o.orderDate >= :start AND o.orderDate < :end: lọc đơn hàng trong khoảng [start, end)
    // Tham số: start - thời điểm bắt đầu, end - thời điểm kết thúc (không bao gồm)
    // Trả về: tổng doanh thu (Double), 0 nếu không có đơn hàng nào
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate >= :start AND o.orderDate < :end")
    Double getRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    // Thống kê doanh thu theo từng ngày trong khoảng thời gian (native SQL)
    // Dùng cho biểu đồ cột 7 ngày trên dashboard admin, hiển thị doanh thu mỗi ngày
    // Logic native SQL:
    //   - CAST(o.order_date AS DATE): chuyển timestamp về chỉ lấy phần ngày (bỏ giờ:phút:giây)
    //   - COALESCE(SUM(o.total_amount), 0): tính tổng tiền mỗi ngày, mặc định 0 nếu không có đơn
    //   - GROUP BY CAST(o.order_date AS DATE): gom nhóm theo từng ngày
    //   - ORDER BY CAST(o.order_date AS DATE): sắp xếp tăng dần theo ngày
    // Tham số: start - thời điểm bắt đầu, end - thời điểm kết thúc
    // Trả về: List<Object[]> mỗi dòng gồm [ngày (Date), tổng doanh thu (Double)]
    @Query(value = "SELECT CAST(o.order_date AS DATE) as day, COALESCE(SUM(o.total_amount), 0) as total " +
           "FROM orders o WHERE o.order_date >= :start AND o.order_date < :end " +
           "GROUP BY CAST(o.order_date AS DATE) ORDER BY CAST(o.order_date AS DATE)", nativeQuery = true)
    List<Object[]> getDailyRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
