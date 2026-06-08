package com.bookstore.repository;

import com.bookstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Tìm sản phẩm theo tên (không phân biệt hoa thường, có phân trang)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    // Tìm sản phẩm theo danh mục
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
    // Tìm theo cả tên lẫn danh mục
    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(String name, Integer categoryId, Pageable pageable);
    // Query linh hoạt: lọc theo tên VÀ danh mục (cả 2 đều có thể null)
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
