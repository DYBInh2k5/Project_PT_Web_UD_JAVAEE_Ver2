package com.bookstore.controller;

import com.bookstore.entity.Category;
import com.bookstore.entity.Product;
import com.bookstore.service.CategoryService;
import com.bookstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

// Controller cho trang chủ và cửa hàng (public, không yêu cầu đăng nhập)
// Xử lý các URL: GET / (trang chủ), GET /shop (trang cửa hàng)
// Phụ thuộc: ProductService (truy vấn sản phẩm), CategoryService (truy vấn danh mục)
@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Xử lý GET / - Trang chủ: hiển thị danh sách sản phẩm với phân trang, lọc theo tên/danh mục, sắp xếp
    // Tham số: page (số trang, mặc định 0), name (lọc theo tên, không bắt buộc),
    //          category (lọc theo ID danh mục, không bắt buộc), sort (sắp xếp, không bắt buộc)
    // B1: Lấy toàn bộ danh mục để hiển thị bộ lọc
    // B2: Gọi ProductService.findFiltered() để lấy trang sản phẩm theo điều kiện (12 sản phẩm/trang)
    // B3: Đưa tất cả dữ liệu vào Model để Thymeleaf render
    // B4: Trả về template "index"
    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer category,
                       @RequestParam(required = false) String sort) {
        List<Category> categories = categoryService.findAll();
        Page<Product> products = productService.findFiltered(name, category, page, 12, sort);

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("name", name);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("sort", sort);
        return "index";
    }

    // Xử lý GET /shop - Trang cửa hàng (alias của trang chủ, cùng chức năng)
    // Tham số: page (số trang, mặc định 0), name (lọc theo tên, không bắt buộc),
    //          category (lọc theo ID danh mục, không bắt buộc), sort (sắp xếp, không bắt buộc)
    // B1: Lấy toàn bộ danh mục để hiển thị bộ lọc
    // B2: Gọi ProductService.findFiltered() để lấy trang sản phẩm (12 sản phẩm/trang)
    // B3: Đưa dữ liệu vào Model để Thymeleaf render
    // B4: Trả về template "shop" (khác template "index" ở trang chủ)
    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer category,
                       @RequestParam(required = false) String sort) {
        List<Category> categories = categoryService.findAll();
        Page<Product> products = productService.findFiltered(name, category, page, 12, sort);

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("name", name);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("sort", sort);
        return "shop";
    }
}
