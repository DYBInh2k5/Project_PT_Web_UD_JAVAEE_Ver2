package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.dto.RegisterDto;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service xử lý logic đăng ký, tra cứu người dùng (User)
// Phụ thuộc: UserRepository để thao tác CSDL, PasswordEncoder để mã hóa mật khẩu
// Cung cấp chức năng: đăng ký tài khoản mới (mặc định role CUSTOMER), tìm kiếm user theo username/ID
// Được sử dụng bởi RegisterController và các component liên quan đến xác thực người dùng
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng ký tài khoản mới với thông tin từ RegisterDto
    // Bước 1: Kiểm tra username đã tồn tại trong CSDL chưa
    // Bước 2: Nếu đã tồn tại -> ném RuntimeException với thông báo lỗi
    // Bước 3: Tạo đối tượng User mới, gán từng trường từ DTO
    // Bước 4: Mã hóa mật khẩu bằng BCrypt trước khi lưu (không lưu plain-text)
    // Bước 5: Gán mặc định role = "CUSTOMER" (người mua hàng thông thường)
    // Bước 6: Lưu user vào CSDL và trả về đối tượng đã lưu (kèm ID tự sinh)
    // Edge case: email/phone/address có thể null -> vẫn lưu được (tùy cấu hình DB)
    public User register(RegisterDto dto) {
        // Kiểm tra username duy nhất, tránh trùng lặp tài khoản
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));  // Mã hóa mật khẩu bằng BCrypt trước khi lưu vào CSDL
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setRole("CUSTOMER");  // Mặc định tài khoản mới là khách hàng, không phải admin
        return userRepository.save(user);  // Lưu và trả về user đã có ID
    }

    // Tìm user theo tên đăng nhập (username)
    // Trả về đối tượng User nếu tìm thấy, null nếu không tồn tại
    // Được sử dụng bởi CustomUserDetailsService khi xác thực đăng nhập
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Tìm user theo ID duy nhất
    // Trả về đối tượng User nếu tìm thấy, null nếu không tồn tại
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}
