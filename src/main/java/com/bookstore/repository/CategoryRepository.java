package com.bookstore.repository;

import com.bookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repository quản lý truy vấn dữ liệu danh mục sản phẩm (Category)
// Sử dụng các phương thức mặc định từ JpaRepository như findAll(), findById(), save(), delete()
// Dùng để hiển thị danh sách danh mục trên các trang lọc sản phẩm và quản lý danh mục
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Kế thừa các phương thức mặc định:
    //   - findAll(): lấy tất cả danh mục, dùng để đổ dữ liệu vào dropdown/bộ lọc trên trang sản phẩm
    //   - findById(id): tìm danh mục theo ID, dùng khi thêm/sửa sản phẩm để lấy category reference
    //   - save(entity): thêm mới hoặc cập nhật danh mục (dùng trong quản trị)
    //   - deleteById(id): xóa danh mục theo ID (dùng trong quản trị)
}
