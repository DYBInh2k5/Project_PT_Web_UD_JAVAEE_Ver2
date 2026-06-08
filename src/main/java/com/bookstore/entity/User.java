package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Lưu thông tin người dùng (admin và khách hàng)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(50)")
    private String username;                // Tên đăng nhập (duy nhất)

    @Column(nullable = false, length = 255)
    private String password;                // Mật khẩu (đã mã hóa BCrypt)

    @Column(nullable = false, columnDefinition = "NVARCHAR(100)")
    private String fullName;                // Họ tên đầy đủ

    @Column(columnDefinition = "NVARCHAR(100)")
    private String email;                   // Email (không bắt buộc)

    @Column(length = 20)
    private String phone;                   // Số điện thoại

    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;                 // Địa chỉ giao hàng

    @Column(nullable = false, columnDefinition = "NVARCHAR(20)")
    private String role;                    // Vai trò: "ADMIN" hoặc "CUSTOMER"

    @Column(name = "created_at")
    private LocalDateTime createdAt;        // Ngày tạo tài khoản

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
