package com.bookstore.controller.admin;

import com.bookstore.entity.Category;
import com.bookstore.entity.Product;
import com.bookstore.service.CategoryService;
import com.bookstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

// Controller admin: quản lý sản phẩm (CRUD + upload ảnh) - yêu cầu quyền ADMIN
// Base URL: /admin/products - Spring Security chặn tất cả URL /admin/** yêu cầu role ADMIN
// Phụ thuộc: ProductService (thao tác với sản phẩm), CategoryService (lấy danh mục)
// Upload ảnh: lưu vào thư mục target/classes/static/images/ để Spring Boot serve như static resource
@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Thư mục lưu ảnh (target/classes/static/images/ để Spring Boot serve được)
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/target/classes/static/images/";

    // Xử lý GET /admin/products - Danh sách sản phẩm (phân trang, lọc theo tên/danh mục)
    // Tham số: page (số trang, mặc định 0), name (lọc theo tên, không bắt buộc),
    //          categoryId (lọc theo ID danh mục, không bắt buộc)
    // B1: Gọi ProductService.findFiltered() để lấy trang sản phẩm (10 sản phẩm/trang, không sắp xếp)
    // B2: Lấy toàn bộ danh mục để hiển thị bộ lọc trên giao diện
    // B3: Đưa dữ liệu sản phẩm, danh mục và thông tin phân trang vào Model
    // B4: Trả về template "admin/products"
    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer categoryId) {
        Page<Product> products = productService.findFiltered(name, categoryId, page, 10, null);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("name", name);
        model.addAttribute("categoryId", categoryId);
        return "admin/products";
    }

    // Xử lý GET /admin/products/add - Hiển thị form thêm sản phẩm mới
    // B1: Khởi tạo Product rỗng và gán Category rỗng để Thymeleaf binding không bị lỗi
    // B2: Đưa product và danh sách categories vào Model
    // B3: Trả về template "admin/product-form" (dùng chung cho cả thêm và sửa)
    @GetMapping("/add")
    public String addForm(Model model) {
        Product product = new Product();
        product.setCategory(new Category());
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    // Xử lý POST /admin/products/add - Xử lý thêm sản phẩm mới (có upload ảnh)
    // Tham số: product (Product được binding từ form), imageFile (file ảnh upload từ input type="file")
    // B1: Nếu có file ảnh được upload (không rỗng) -> gọi saveImage() lưu file và gán tên file vào product
    // B2: Lấy Category từ database theo ID (tránh lazy loading hoặc transient entity)
    // B3: Gọi ProductService.save(product) để lưu vào database
    // B4: Nếu thành công -> flash success + redirect /admin/products
    // B5: Nếu có ngoại lệ -> flash error + redirect /admin/products
    @PostMapping("/add")
    public String add(@ModelAttribute Product product,
                      @RequestParam("imageFile") MultipartFile imageFile,
                      RedirectAttributes ra) {
        try {
            if (!imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                product.setImage(fileName);
            }
            product.setCategory(categoryService.findById(product.getCategory().getId()));
            productService.save(product);
            ra.addFlashAttribute("success", "Thêm sản phẩm thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    // Xử lý GET /admin/products/edit/{id} - Hiển thị form sửa sản phẩm (load dữ liệu cũ)
    // Tham số: id (ID sản phẩm cần sửa từ URL path)
    // B1: Tìm Product theo id, nếu null -> redirect /admin/products
    // B2: Đưa product (đã có dữ liệu cũ) và danh sách categories vào Model
    // B3: Trả về template "admin/product-form" (dùng chung form với thêm mới)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        if (product == null) return "redirect:/admin/products";
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    // Xử lý POST /admin/products/edit/{id} - Xử lý cập nhật thông tin sản phẩm
    // Tham số: id (ID sản phẩm từ URL path), product (dữ liệu mới từ form),
    //          imageFile (file ảnh mới, có thể rỗng nếu không đổi ảnh)
    // B1: Tìm Product existing từ database, nếu null -> redirect /admin/products
    // B2: Cập nhật các trường: name, author, description, price, stock, category
    // B3: Nếu có file ảnh mới -> gọi saveImage() lưu file và cập nhật existing.setImage()
    // B4: Gọi ProductService.save(existing) để lưu thay đổi vào database
    // B5: Flash success/error + redirect /admin/products
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                       @ModelAttribute Product product,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       RedirectAttributes ra) {
        try {
            Product existing = productService.findById(id);
            if (existing == null) return "redirect:/admin/products";
            existing.setName(product.getName());
            existing.setAuthor(product.getAuthor());
            existing.setDescription(product.getDescription());
            existing.setPrice(product.getPrice());
            existing.setStock(product.getStock());
            existing.setCategory(categoryService.findById(product.getCategory().getId()));
            if (!imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                existing.setImage(fileName);
            }
            productService.save(existing);
            ra.addFlashAttribute("success", "Cập nhật sản phẩm thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    // Xử lý GET /admin/products/delete/{id} - Xóa sản phẩm (xóa cả file ảnh trên disk)
    // Tham số: id (ID sản phẩm cần xóa từ URL path)
    // B1: Tìm Product theo id, nếu tồn tại -> lấy tên file ảnh
    // B2: Nếu có ảnh (image != null và không rỗng) -> xóa file vật lý trong thư mục UPLOAD_DIR
    // B3: Gọi ProductService.deleteById(id) để xóa bản ghi trong database
    // B4: Flash success/error + redirect /admin/products
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            Product product = productService.findById(id);
            if (product != null) {
                String image = product.getImage();
                if (image != null && !image.isEmpty()) {
                    File imgFile = new File(UPLOAD_DIR + image);
                    if (imgFile.exists()) imgFile.delete();
                }
                productService.deleteById(id);
            }
            ra.addFlashAttribute("success", "Xóa sản phẩm thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa sản phẩm này");
        }
        return "redirect:/admin/products";
    }

    // Helper: Lưu file ảnh upload vào thư mục static/images/, trả về tên file để lưu vào database
    // B1: Kiểm tra thư mục UPLOAD_DIR tồn tại chưa, nếu chưa thì tạo mới
    // B2: Tạo tên file duy nhất = UUID ngẫu nhiên + "_" + tên file gốc (tránh trùng lặp)
    // B3: Copy dữ liệu từ MultipartFile vào file đích, ghi đè nếu đã tồn tại
    // B4: Trả về tên file (fileName) để gán vào Product.image
    private String saveImage(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir, fileName);
        Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
