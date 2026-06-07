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

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

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
