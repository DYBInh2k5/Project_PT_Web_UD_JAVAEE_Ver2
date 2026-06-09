package com.bookstore.repository;

import com.bookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Repository quản lý truy vấn dữ liệu chi tiết sản phẩm trong giỏ hàng (CartItem)
// Mỗi CartItem đại diện cho một sản phẩm với số lượng nhất định trong giỏ hàng
// Cung cấp phương thức kiểm tra trùng lặp sản phẩm và xóa toàn bộ giỏ sau khi thanh toán
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    // Tìm một món hàng trong giỏ theo ID giỏ hàng và ID sản phẩm
    // Dùng khi thêm sản phẩm vào giỏ: nếu sản phẩm đã tồn tại thì tăng số lượng thay vì thêm dòng mới
    // Tham số: cartId - ID của giỏ hàng, productId - ID của sản phẩm cần kiểm tra
    // Trả về: Optional<CartItem> chứa bản ghi nếu sản phẩm đã có trong giỏ, rỗng nếu chưa có
    Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);
    // Xóa tất cả các món hàng trong một giỏ hàng cụ thể
    // Dùng sau khi người dùng thanh toán thành công: xóa sạch giỏ hàng để chuẩn bị cho lần mua sau
    // Tham số: cartId - ID của giỏ hàng cần xóa toàn bộ sản phẩm
    void deleteByCartId(Integer cartId);
}
