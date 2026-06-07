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

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/target/classes/static/images/";

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

    @GetMapping("/add")
    public String addForm(Model model) {
        Product product = new Product();
        product.setCategory(new Category());
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

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

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);
        if (product == null) return "redirect:/admin/products";
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

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

    private String saveImage(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir, fileName);
        Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
