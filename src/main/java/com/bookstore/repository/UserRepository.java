package com.bookstore.repository;

import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Repository quản lý truy vấn dữ liệu người dùng (User)
// Cung cấp các phương thức xác thực và kiểm tra tồn tại cho tính năng đăng nhập và đăng ký
public interface UserRepository extends JpaRepository<User, Integer> {
    // Truy vấn thông tin user theo tên đăng nhập (username)
    // Dùng trong chức năng đăng nhập: kiểm tra thông tin user có tồn tại trong hệ thống hay không
    // Tham số: username - tên đăng nhập cần tra cứu
    // Trả về: Optional<User> chứa user nếu tìm thấy, rỗng nếu không tồn tại
    Optional<User> findByUsername(String username);
    // Kiểm tra xem tên đăng nhập đã có người sử dụng chưa
    // Dùng trong chức năng đăng ký tài khoản mới để tránh trùng lặp username
    // Tham số: username - tên đăng nhập cần kiểm tra
    // Trả về: true nếu username đã tồn tại trong CSDL, false nếu chưa có
    boolean existsByUsername(String username);
}
