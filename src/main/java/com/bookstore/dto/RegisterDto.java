package com.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO (Data Transfer Object) nhận dữ liệu từ form đăng ký tài khoản
// Các annotation jakarta.validation ràng buộc dữ liệu đầu vào trước khi xử lý
public class RegisterDto {
    // @NotBlank: kiểm tra tên đăng nhập không được null/trống/chỉ khoảng trắng
    @NotBlank(message = "Tên đăng nhập không được để trống")
    // @Size: giới hạn độ dài từ 3 đến 50 ký tự (ràng buộc ở tầng controller trước khi vào DB)
    @Size(min = 3, max = 50, message = "Tên đăng nhập từ 3-50 ký tự")
    private String username;

    // @NotBlank: mật khẩu bắt buộc nhập
    @NotBlank(message = "Mật khẩu không được để trống")
    // @Size(min=6): mật khẩu phải có tối thiểu 6 ký tự để đảm bảo độ an toàn cơ bản
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;

    // @NotBlank: họ tên là trường bắt buộc, dùng cho thông tin giao hàng
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    // Email không bắt buộc (không có @NotBlank), có thể để trống khi đăng ký
    private String email;
    // Số điện thoại không bắt buộc, dùng để liên hệ khi giao hàng
    private String phone;
    // Địa chỉ không bắt buộc, lưu thông tin giao hàng mặc định
    private String address;

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
}
