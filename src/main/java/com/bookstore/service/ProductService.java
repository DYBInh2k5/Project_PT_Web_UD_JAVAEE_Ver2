package com.bookstore.service;

import com.bookstore.entity.Product;
import com.bookstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

// Service xử lý logic nghiệp vụ liên quan đến sản phẩm (Product)
// Phụ thuộc: ProductRepository để thao tác với CSDL
// Cung cấp các chức năng: CRUD, tìm kiếm, lọc, sắp xếp và phân trang sản phẩm
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy danh sách tất cả sản phẩm với phân trang, sắp xếp theo ID mới nhất xuống dưới
    // Tham số page: số trang bắt đầu từ 0, size: số lượng sản phẩm mỗi trang
    // Trả về đối tượng Page chứa danh sách sản phẩm và thông tin phân trang
    public Page<Product> findAll(int page, int size) {
        // Tạo PageRequest với page, size và Sort giảm dần theo id để sản phẩm mới nhất hiện trước
        return productRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    // Tìm kiếm + lọc sản phẩm theo tên, danh mục, kèm sắp xếp và phân trang
    // Tham số name: tên sản phẩm cần tìm (có thể null), categoryId: ID danh mục (có thể null)
    // Tham số sort: "asc" sắp theo giá tăng dần, "desc" giảm dần, null/mặc định theo ID mới nhất
    // Trả về Page<Product> đã được lọc và sắp xếp
    // Xử lý 4 trường hợp: có cả tên + danh mục, chỉ tên, chỉ danh mục, không có điều kiện
    public Page<Product> findFiltered(String name, Integer categoryId, int page, int size, String sort) {
        Pageable pageable;
        // Xác định cách sắp xếp dựa trên tham số sort
        if ("asc".equals(sort)) {
            // Sắp xếp giá từ thấp đến cao
            pageable = PageRequest.of(page, size, Sort.by("price").ascending());
        } else if ("desc".equals(sort)) {
            // Sắp xếp giá từ cao xuống thấp
            pageable = PageRequest.of(page, size, Sort.by("price").descending());
        } else {
            // Mặc định: sắp xếp theo ID mới nhất
            pageable = PageRequest.of(page, size, Sort.by("id").descending());
        }
        // Trường hợp 1: có cả tên và danh mục -> tìm kiếm kết hợp cả hai điều kiện
        if (name != null && !name.trim().isEmpty() && categoryId != null) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryId(name.trim(), categoryId, pageable);
        // Trường hợp 2: chỉ có tên -> tìm kiếm theo tên không phân biệt hoa thường
        } else if (name != null && !name.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
        // Trường hợp 3: chỉ có danh mục -> lọc theo danh mục
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
        // Trường hợp 4: không có điều kiện lọc -> lấy tất cả
        return productRepository.findAll(pageable);
    }

    // Tìm sản phẩm theo ID duy nhất
    // Trả về đối tượng Product nếu tìm thấy, null nếu không tồn tại
    public Product findById(Integer id) {
        // orElse(null) xử lý trường hợp Optional rỗng -> tránh ném NoSuchElementException
        return productRepository.findById(id).orElse(null);
    }

    // Thêm mới hoặc cập nhật sản phẩm (Spring Data JPA tự xác định insert/update dựa trên ID)
    // Nếu Product.id == null -> thêm mới, nếu đã có ID -> cập nhật
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm theo ID
    // Lưu ý: nếu sản phẩm không tồn tại, phương thức sẽ không làm gì (không ném lỗi)
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}
