package com.bookstore.service;

import com.bookstore.entity.Category;
import com.bookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// Service xử lý logic nghiệp vụ danh mục sản phẩm (Category)
// Phụ thuộc: CategoryRepository để thao tác với CSDL
// Cung cấp các chức năng CRUD cơ bản cho danh mục, dùng để phân loại sản phẩm và lọc (filter)
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy danh sách tất cả danh mục (dùng cho dropdown lọc sản phẩm, form chọn danh mục)
    // Trả về List<Category> chứa toàn bộ danh mục trong CSDL
    // Edge case: nếu chưa có danh mục nào, trả về danh sách rỗng (không null)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Tìm danh mục theo ID duy nhất
    // Trả về đối tượng Category nếu tìm thấy, null nếu không tồn tại (tránh lỗi NullPointerException)
    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Thêm mới hoặc cập nhật danh mục
    // Nếu Category.id == null -> thêm mới, nếu đã có ID -> cập nhật thông tin danh mục
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // Xóa danh mục theo ID
    // Lưu ý: nếu danh mục đang chứa sản phẩm, có thể gây lỗi vi phạm khóa ngoại (FK constraint)
    // Cần xử lý trước khi xóa nếu có ràng buộc
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
}
