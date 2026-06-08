package com.bookstore.repository;

import com.bookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    // Tìm món hàng trong giỏ theo cartId và productId (để kiểm tra trùng)
    Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);
    // Xóa tất cả món trong giỏ (dùng khi thanh toán xong)
    void deleteByCartId(Integer cartId);
}
