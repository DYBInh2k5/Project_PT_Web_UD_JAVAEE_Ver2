package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.dto.RegisterDto;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Xử lý logic đăng ký, tra cứu người dùng
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng ký tài khoản mới (mặc định role = "CUSTOMER")
    public User register(RegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));  // Mã hóa mật khẩu BCrypt
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setRole("CUSTOMER");
        return userRepository.save(user);
    }

    // Tìm user theo tên đăng nhập
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Tìm user theo ID
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}
