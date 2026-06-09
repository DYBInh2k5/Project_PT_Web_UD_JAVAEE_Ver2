package com.bookstore.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Lớp thực thể đại diện cho giỏ hàng của người dùng trong hệ thống BookStore.
// Entity này ánh xạ tới bảng "carts". Mỗi người dùng (User) chỉ có duy nhất một giỏ hàng (OneToOne).
// Giỏ hàng chứa nhiều CartItem (OneToMany), mỗi CartItem đại diện cho một sản phẩm với số lượng cụ thể.
// Khi giỏ hàng bị xóa, tất cả CartItem liên quan cũng bị xóa theo nhờ cascade = CascadeType.ALL và orphanRemoval = true.
// Quan hệ chính:
// - Một Cart thuộc về một User (OneToOne).
// - Một Cart có nhiều CartItem (OneToMany).
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng, khóa chính của bảng carts, được sinh tự động bởi cơ sở dữ liệu

    // Quan hệ OneToOne với User: mỗi giỏ hàng thuộc về một người dùng duy nhất.
    // FetchType.LAZY giúp trì hoãn việc load đối tượng User cho đến khi thực sự cần.
    // @JoinColumn ánh xạ tới khóa ngoại "user_id", unique = true đảm bảo mỗi user chỉ có một giỏ hàng.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;                      // Người sở hữu giỏ hàng, quan hệ một-một với User

    // Quan hệ OneToMany với CartItem: một giỏ hàng chứa nhiều món hàng.
    // mappedBy = "cart" cho biết trường "cart" bên phía CartItem là chủ sở hữu quan hệ.
    // cascade = CascadeType.ALL: mọi thao tác trên Cart đều được áp dụng cho CartItem (persist, merge, remove...).
    // orphanRemoval = true: tự động xóa CartItem khỏi database khi bị loại khỏi danh sách.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();  // Danh sách các món hàng có trong giỏ, khởi tạo là ArrayList rỗng

    public Cart() {}

    public Cart(User user) { this.user = user; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
