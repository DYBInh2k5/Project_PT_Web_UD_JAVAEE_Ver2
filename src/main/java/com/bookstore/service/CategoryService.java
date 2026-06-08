package com.bookstore.service;

import com.bookstore.entity.Category;
import com.bookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// Xử lý logic nghiệp vụ danh mục sản phẩm
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy tất cả danh mục (dùng cho filter, dropdown)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Tìm danh mục theo ID
    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Thêm hoặc sửa danh mục
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // Xóa danh mục
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
}
