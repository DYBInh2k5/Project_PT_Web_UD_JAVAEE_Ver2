package com.bookstore.repository;

import com.bookstore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repository quản lý truy vấn dữ liệu chi tiết đơn hàng (OrderDetail)
// OrderDetail lưu thông tin từng sản phẩm trong một đơn hàng (sản phẩm, số lượng, giá)
// Sử dụng các phương thức mặc định từ JpaRepository để thao tác dữ liệu
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Kế thừa các phương thức mặc định:
    //   - findAll(): lấy tất cả chi tiết đơn hàng (dùng trong admin để xem báo cáo)
    //   - findByOrderId(orderId): không định nghĩa nhưng Spring Data có thể sinh tự động nếu cần
    //   - save(entity): thêm mới chi tiết đơn hàng khi người dùng đặt hàng thành công
    //   - deleteById(id): xóa chi tiết đơn hàng khi cần hủy đơn hoặc sửa đơn (admin)
    // Lưu ý: repository này chủ yếu được gọi gián tiếp qua Order, ít khi dùng trực tiếp ở controller
}
