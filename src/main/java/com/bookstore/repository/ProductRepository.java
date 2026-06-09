package com.bookstore.repository;

import com.bookstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
// Repository quản lý truy vấn dữ liệu sản phẩm (Product)
// Cung cấp các phương thức tìm kiếm, lọc sản phẩm theo tên, danh mục với phân trang
// Hỗ trợ tìm kiếm linh hoạt không phân biệt chữ hoa/thường
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Tìm sản phẩm theo tên, không phân biệt hoa thường, hỗ trợ phân trang
    // Dùng trên trang danh mục sản phẩm và thanh tìm kiếm
    // Tham số: name - chuỗi tìm kiếm (tìm tên chứa chuỗi này), pageable - thông tin trang và sắp xếp
    // JPQL sinh tự động: WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    // Tìm sản phẩm theo ID danh mục, hỗ trợ phân trang
    // Dùng trên trang danh mục sản phẩm khi người dùng chọn một category cụ thể
    // Tham số: categoryId - ID của danh mục, pageable - thông tin trang và sắp xếp
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
    // Tìm sản phẩm theo cả tên lẫn danh mục, không phân biệt hoa thường, hỗ trợ phân trang
    // Dùng khi người dùng vừa gõ tìm kiếm vừa chọn danh mục lọc
    // Tham số: name - chuỗi tìm kiếm, categoryId - ID danh mục, pageable - thông tin trang
    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(String name, Integer categoryId, Pageable pageable);
    // Query JPQL linh hoạt: lọc sản phẩm theo tên VÀ danh mục, cả 2 tham số đều có thể null
    // Dùng trên trang tìm kiếm nâng cao, cho phép bỏ qua điều kiện lọc nếu không được chỉ định
    // Logic JPQL:
    //   - (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))):
    //     Nếu name = null thì bỏ qua điều kiện tên; ngược lại tìm tên chứa chuỗi (không phân biệt hoa/thường)
    //   - (:categoryId IS NULL OR p.category.id = :categoryId):
    //     Nếu categoryId = null thì bỏ qua điều kiện danh mục; ngược lại lọc đúng theo ID danh mục
    // countQuery: truy vấn đếm số lượng tương ứng để Spring Data tính tổng số trang
    // Tham số: name - chuỗi tìm kiếm (có thể null), categoryId - ID danh mục (có thể null)
    @Query(value = "SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId)",
           countQuery = "SELECT COUNT(p) FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findFiltered(@Param("name") String name,
                                @Param("categoryId") Integer categoryId,
                                Pageable pageable);
}
