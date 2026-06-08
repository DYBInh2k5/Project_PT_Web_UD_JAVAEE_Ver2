package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Lưu thông tin sách (sản phẩm)
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng

    @Column(nullable = false, columnDefinition = "NVARCHAR(200)")
    private String name;                    // Tên sách

    @Column(columnDefinition = "NVARCHAR(100)")
    private String author;                  // Tác giả

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;             // Mô tả sách (hỗ trợ tiếng Việt)

    @Column(nullable = false)
    private Double price;                   // Giá bán

    @Column(nullable = false)
    private Integer stock;                  // Số lượng tồn kho

    @Column(length = 255)
    private String image;                   // Tên file ảnh (lưu trong static/images/)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;              // Danh mục (khóa ngoại)

    @Column(name = "created_at")
    private LocalDateTime createdAt;        // Ngày thêm sản phẩm

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Product() {}

    public Product(String name, String author, String description, Double price, Integer stock, String image, Category category) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.category = category;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
