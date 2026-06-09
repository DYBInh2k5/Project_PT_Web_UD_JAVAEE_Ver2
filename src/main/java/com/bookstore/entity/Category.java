package com.bookstore.entity;

import jakarta.persistence.*;
import java.util.List;

// Lớp thực thể đại diện cho một danh mục sách trong hệ thống BookStore, ví dụ: Kinh tế, Văn học, Khoa học, Thiếu nhi...
// Entity này ánh xạ tới bảng "categories" và lưu trữ tên danh mục. Mỗi danh mục có thể chứa nhiều sản phẩm (Product)
// thông qua quan hệ OneToMany. Danh mục giúp phân loại và nhóm các sách có cùng chủ đề để thuận tiện cho việc tìm kiếm
// và hiển thị theo danh mục trên giao diện người dùng.
// Quan hệ chính:
// - Một Category có nhiều Product (OneToMany).
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng, khóa chính của bảng categories, được sinh tự động bởi cơ sở dữ liệu

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(100)")
    private String name;                    // Tên danh mục, phải là duy nhất trong hệ thống, không được null, hỗ trợ tiếng Việt

    // Quan hệ OneToMany với Product: một danh mục có nhiều sản phẩm.
    // mappedBy = "category" cho biết trường "category" bên phía Product là chủ sở hữu quan hệ (khóa ngoại).
    @OneToMany(mappedBy = "category")
    private List<Product> products;         // Danh sách các sản phẩm (sách) thuộc về danh mục này, được load khi cần

    public Category() {}

    public Category(String name) { this.name = name; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
