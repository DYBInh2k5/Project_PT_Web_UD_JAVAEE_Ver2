package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Lớp thực thể đại diện cho người dùng của hệ thống BookStore, bao gồm cả quản trị viên (ADMIN) và khách hàng (CUSTOMER).
// Entity này ánh xạ tới bảng "users" trong cơ sở dữ liệu và chịu trách nhiệm lưu trữ thông tin xác thực, thông tin cá nhân
// và vai trò của người dùng. Mỗi User có thể sở hữu một giỏ hàng (Cart) và có thể đặt nhiều đơn hàng (Order).
// Mật khẩu được lưu dưới dạng đã mã hóa BCrypt để đảm bảo an toàn bảo mật. Quan hệ chính:
// - Một User có một Cart (OneToOne).
// - Một User có nhiều Order (OneToMany).
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng, khóa chính của bảng, được sinh tự động bởi cơ sở dữ liệu

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(50)")
    private String username;                // Tên đăng nhập duy nhất của người dùng, không được null, hỗ trợ tiếng Việt

    @Column(nullable = false, length = 255)
    private String password;                // Mật khẩu đã được mã hóa bằng thuật toán BCrypt, không được null, độ dài tối đa 255 ký tự

    @Column(nullable = false, columnDefinition = "NVARCHAR(100)")
    private String fullName;                // Họ và tên đầy đủ của người dùng, không được null, hỗ trợ tiếng Việt

    @Column(columnDefinition = "NVARCHAR(100)")
    private String email;                   // Địa chỉ email của người dùng, không bắt buộc (có thể null), hỗ trợ tiếng Việt

    @Column(length = 20)
    private String phone;                   // Số điện thoại liên lạc, không bắt buộc, độ dài tối đa 20 ký tự

    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;                 // Địa chỉ giao hàng mặc định của người dùng, hỗ trợ tiếng Việt

    @Column(nullable = false, columnDefinition = "NVARCHAR(20)")
    private String role;                    // Vai trò của người dùng trong hệ thống: "ADMIN" (quản trị viên) hoặc "CUSTOMER" (khách hàng)

    @Column(name = "created_at")
    private LocalDateTime createdAt;        // Thời điểm tài khoản được tạo, tự động gán khi persist lần đầu

    // Phương thức này được gọi tự động trước khi entity được persist lần đầu tiên nhờ annotation @PrePersist.
    // Gán thời gian hiện tại cho trường createdAt nếu chưa được thiết lập.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public User() {}

    public User(String username, String password, String fullName, String email, String phone, String address, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
