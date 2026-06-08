package com.bookstore.repository;

import com.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Tìm giỏ hàng theo ID người dùng (mỗi user chỉ 1 giỏ)
    Optional<Cart> findByUserId(Integer userId);
}
