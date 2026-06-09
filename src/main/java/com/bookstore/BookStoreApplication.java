package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Điểm khởi đầu (entry point) của ứng dụng Spring Boot BookStore
// @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
// Spring Boot sẽ tự động quét các component, cấu hình và khởi động embedded server
@SpringBootApplication
public class BookStoreApplication {
    // Phương thức main gọi SpringApplication.run() để bootstrap toàn bộ ứng dụng
    // Tham số args cho phép truyền các đối số dòng lệnh (VD: --server.port=8081)
    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }
}
