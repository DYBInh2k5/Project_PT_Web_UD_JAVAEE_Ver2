package com.bookstore.entity;

import jakarta.persistence.*;

// Lớp thực thể đại diện cho một món hàng cụ thể trong giỏ hàng của hệ thống BookStore, kết hợp giữa sản phẩm (Product)
// và số lượng tương ứng. Entity này ánh xạ tới bảng "cart_items". Mỗi CartItem thuộc về một giỏ hàng (Cart) và tham chiếu
// đến một sản phẩm (Product) cụ thể. Đây là bảng trung gian thực hiện quan hệ nhiều-nhiều giữa Cart và Product.
// Quan hệ chính:
// - Nhiều CartItem thuộc một Cart (ManyToOne).
// - Nhiều CartItem tham chiếu một Product (ManyToOne).
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng, khóa chính của bảng cart_items, được sinh tự động bởi cơ sở dữ liệu

    // Quan hệ ManyToOne với Cart: nhiều món hàng có thể thuộc về một giỏ hàng.
    // FetchType.LAZY giúp trì hoãn load giỏ hàng cho đến khi thực sự cần.
    // @JoinColumn ánh xạ tới khóa ngoại "cart_id", không được null.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;                      // Giỏ hàng chứa món hàng này, khóa ngoại tham chiếu đến bảng carts

    // Quan hệ ManyToOne với Product: nhiều món hàng trong giỏ có thể tham chiếu đến cùng một sản phẩm.
    // FetchType.LAZY giúp trì hoãn load sản phẩm cho đến khi thực sự cần.
    // @JoinColumn ánh xạ tới khóa ngoại "product_id", không được null.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;                // Sản phẩm được chọn, khóa ngoại tham chiếu đến bảng products

    @Column(nullable = false)
    private Integer quantity;               // Số lượng sản phẩm mà người dùng muốn mua, phải là số dương, không được null

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
