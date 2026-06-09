package com.bookstore.config;

import com.bookstore.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// Cấu hình bảo mật Spring Security: phân quyền truy cập URL, form đăng nhập, đăng xuất
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Tạo bean mã hóa mật khẩu dùng thuật toán BCrypt (mạnh, có salt tự động)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Tạo bean xác thực dùng UserDetailsService tùy chỉnh kết hợp với PasswordEncoder
    // DaoAuthenticationProvider sẽ gọi loadUserByUsername() và tự động kiểm tra mật khẩu
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Định nghĩa chuỗi filter bảo mật: phân quyền URL, form login, logout, xử lý lỗi
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(config -> config
                // Chỉ tài khoản có vai trò ADMIN mới được truy cập trang /admin/**
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Cả CUSTOMER và ADMIN đều được truy cập giỏ hàng và đặt hàng
                .requestMatchers("/cart/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers("/order/**").hasAnyRole("CUSTOMER", "ADMIN")
                // Cho phép tất cả truy cập trang đăng ký, đăng nhập, tài nguyên tĩnh và trang chủ
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**", "/").permitAll()
                // Cho phép tất cả truy cập trang shop và API (công khai)
                .requestMatchers("/shop/**", "/api/**").permitAll()
                // Các URL còn lại đều yêu cầu xác thực
                .anyRequest().authenticated()
            )
            // Cấu hình form đăng nhập tùy chỉnh
            .formLogin(form -> form
                .loginPage("/login")                 // Trang đăng nhập tự tạo
                .defaultSuccessUrl("/")               // Sau khi đăng nhập thành công, chuyển về trang chủ
                .failureUrl("/login?error=true")      // Sai thông tin -> chuyển hướng kèm tham số lỗi
                .permitAll()                          // Cho phép tất cả truy cập trang login
            )
            // Cấu hình đăng xuất
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  // URL kích hoạt logout
                .logoutSuccessUrl("/")                 // Sau khi logout, chuyển về trang chủ
                .invalidateHttpSession(true)           // Hủy session hiện tại
                .clearAuthentication(true)             // Xóa thông tin xác thực
                .permitAll()                           // Cho phép tất cả truy cập hành động logout
            )
            // Cấu hình xử lý ngoại lệ khi không có quyền truy cập
            .exceptionHandling(handler -> handler
                .accessDeniedPage("/login?accessDenied=true")  // Chuyển đến trang login kèm thông báo từ chối
            );
        return http.build();
    }
}
