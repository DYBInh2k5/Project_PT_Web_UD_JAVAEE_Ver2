package com.bookstore.entity;

import jakarta.persistence.*;

// Một món hàng cụ thể trong giỏ (sản phẩm + số lượng)
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;                      // Giỏ hàng chứa món này

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;                // Sản phẩm được chọn

    @Column(nullable = false)
    private Integer quantity;               // Số lượng mua

    public CartItem() {}

    public CartItem(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
