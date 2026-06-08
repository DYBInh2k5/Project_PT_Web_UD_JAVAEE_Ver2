package com.bookstore.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Giỏ hàng của người dùng (mỗi user chỉ có 1 giỏ)
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;                      // Người sở hữu giỏ hàng

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();  // Danh sách món trong giỏ

    public Cart() {}

    public Cart(User user) { this.user = user; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
