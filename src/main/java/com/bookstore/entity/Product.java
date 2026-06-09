package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Lớp thực thể đại diện cho một cuốn sách (sản phẩm) trong hệ thống BookStore.
// Entity này ánh xạ tới bảng "products" và lưu trữ toàn bộ thông tin về sách bao gồm tên, tác giả, mô tả, giá bán,
// số lượng tồn kho và ảnh bìa. Mỗi sản phẩm thuộc về một danh mục (Category) thông qua quan hệ ManyToOne.
// Khi một sản phẩm được thêm vào giỏ hàng, thông tin sẽ được lưu trong CartItem; khi đặt hàng, thông tin được lưu trong OrderDetail.
// Quan hệ chính:
// - Nhiều Product thuộc một Category (ManyToOne).
// - Một Product có thể có trong nhiều CartItem (OneToMany).
// - Một Product có thể xuất hiện trong nhiều OrderDetail (OneToMany).
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng, khóa chính của bảng products, được sinh tự động bởi cơ sở dữ liệu

    @Column(nullable = false, columnDefinition = "NVARCHAR(200)")
    private String name;                    // Tên đầy đủ của cuốn sách, không được null, hỗ trợ tiếng Việt, độ dài tối đa 200 ký tự

    @Column(columnDefinition = "NVARCHAR(100)")
    private String author;                  // Tên tác giả của cuốn sách, không bắt buộc, hỗ trợ tiếng Việt

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;             // Mô tả chi tiết về nội dung sách, có thể rất dài, hỗ trợ tiếng Việt nhờ kiểu NVARCHAR(MAX)

    @Column(nullable = false)
    private Double price;                   // Giá bán hiện tại của sách, không được null, kiểu Double để hỗ trợ số thập phân

    @Column(nullable = false)
    private Integer stock;                  // Số lượng sách còn trong kho, không được null, dùng để kiểm tra tồn kho khi bán hàng

    @Column(length = 255)
    private String image;                   // Tên file ảnh bìa của sách, ảnh được lưu trong thư mục static/images/, độ dài tối đa 255 ký tự

    // Quan hệ ManyToOne với Category: nhiều sản phẩm thuộc về một danh mục.
    // FetchType.LAZY được dùng để tránh n+1 query khi không cần lấy danh mục ngay lập tức.
    // @JoinColumn ánh xạ tới khóa ngoại "category_id" trong bảng products.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;              // Danh mục sách (khóa ngoại tham chiếu đến bảng categories)

    @Column(name = "created_at")
    private LocalDateTime createdAt;        // Thời điểm sản phẩm được thêm vào hệ thống, tự động gán khi persist lần đầu

    // Phương thức này được gọi tự động trước khi entity được persist lần đầu tiên nhờ annotation @PrePersist.
    // Gán thời gian hiện tại cho trường createdAt nếu chưa được thiết lập.
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
