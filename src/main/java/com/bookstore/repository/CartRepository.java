package com.bookstore.repository;

import com.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Repository quản lý truy vấn dữ liệu giỏ hàng (Cart)
// Mỗi người dùng chỉ có một giỏ hàng duy nhất trong hệ thống
// Cung cấp phương thức tìm giỏ hàng theo user để thêm/sửa/xóa sản phẩm trong giỏ
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Tìm giỏ hàng theo ID của người dùng
    // Dùng khi người dùng đăng nhập và truy cập giỏ hàng: lấy giỏ hàng tương ứng với user
    // Mỗi user chỉ có 1 giỏ hàng (quan hệ 1-1 giữa User và Cart)
    // Tham số: userId - ID của người dùng cần tìm giỏ hàng
    // Trả về: Optional<Cart> chứa giỏ hàng nếu tìm thấy, rỗng nếu user chưa có giỏ hàng
    Optional<Cart> findByUserId(Integer userId);
}
