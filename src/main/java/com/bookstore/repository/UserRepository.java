package com.bookstore.repository;

import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Tìm user theo tên đăng nhập (dùng cho login)
    Optional<User> findByUsername(String username);
    // Kiểm tra username đã tồn tại chưa (dùng cho đăng ký)
    boolean existsByUsername(String username);
}
