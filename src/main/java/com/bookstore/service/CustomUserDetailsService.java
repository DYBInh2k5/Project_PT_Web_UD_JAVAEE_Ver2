package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

// Service tích hợp Spring Security: nạp thông tin user từ CSDL khi người dùng đăng nhập
// Triển khai interface UserDetailsService của Spring Security để xác thực (authentication)
// Phụ thuộc: UserRepository để truy vấn thông tin user từ CSDL theo username
// Được Spring Security gọi tự động khi có yêu cầu đăng nhập (form login, basic auth, etc.)
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Nạp thông tin người dùng từ CSDL dựa trên username để Spring Security xác thực
    // Bước 1: Tìm user trong CSDL theo username (gọi UserRepository.findByUsername)
    // Bước 2: Nếu không tìm thấy -> ném UsernameNotFoundException (Spring Security tự xử lý, trả về 401 Unauthorized)
    // Bước 3: Nếu tìm thấy -> tạo đối tượng UserDetails (Spring Security) với:
    //   - username: tên đăng nhập
    //   - password: mật khẩu đã mã hóa (BCrypt) để Spring Security so sánh với password nhập vào
    //   - authorities: quyền của user (ROLE_CUSTOMER hoặc ROLE_ADMIN) dùng cho phân quyền (authorization)
    // Edge case: username không tồn tại -> UsernameNotFoundException được ném và Spring Security trả về lỗi đăng nhập
    // Edge case: user có role null -> sẽ tạo granted authority "ROLE_null" (cần đảm bảo role luôn có giá trị)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user theo username, nếu không có thì ném ngoại lệ UsernameNotFoundException
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Tạo đối tượng UserDetails với thông tin user và quyền (role) tương ứng
        // Collections.singletonList tạo list bất biến chứa một phần tử duy nhất (quyền của user)
        // Tiền tố "ROLE_" được thêm vào role để đúng format của Spring Security (hasRole("ADMIN") tương đương hasAuthority("ROLE_ADMIN"))
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),                                    // Tên đăng nhập
                user.getPassword(),                                    // Mật khẩu đã mã hóa BCrypt (Spring Security tự so sánh)
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))  // Gán quyền dạng ROLE_CUSTOMER hoặc ROLE_ADMIN
        );
    }
}
