package com.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Cấu hình Spring MVC: ánh xạ đường dẫn URL đến thư mục chứa tài nguyên tĩnh (CSS, JS, ảnh) trong classpath
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Khi trình duyệt yêu cầu /css/*, Spring sẽ tìm file trong thư mục classpath:/static/css/
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        // Khi trình duyệt yêu cầu /js/*, Spring sẽ tìm file trong thư mục classpath:/static/js/
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        // Khi trình duyệt yêu cầu /images/*, Spring sẽ tìm file trong thư mục classpath:/static/images/
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }
}
