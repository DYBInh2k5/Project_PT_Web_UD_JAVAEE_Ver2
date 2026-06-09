package com.bookstore.entity;

import jakarta.persistence.*;

// Lớp thực thể đại diện cho chi tiết từng sản phẩm trong một đơn hàng của hệ thống BookStore.
// Entity này ánh xạ tới bảng "order_details". Mỗi OrderDetail ghi lại thông tin về một sản phẩm cụ thể được đặt mua
// trong đơn hàng, bao gồm số lượng và giá tại thời điểm mua. Giá được lưu riêng để đảm bảo tính chính xác của lịch sử
// giao dịch, bởi giá sản phẩm có thể thay đổi sau này. Đây là bảng trung gian thực hiện quan hệ nhiều-nhiều giữa Order và Product.
// Quan hệ chính:
// - Nhiều OrderDetail thuộc một Order (ManyToOne).
// - Nhiều OrderDetail tham chiếu một Product (ManyToOne).
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng, khóa chính của bảng order_details, được sinh tự động bởi cơ sở dữ liệu

    // Quan hệ ManyToOne với Order: nhiều chi tiết đơn hàng thuộc về một đơn hàng.
    // FetchType.LAZY giúp trì hoãn load đơn hàng cho đến khi thực sự cần.
    // @JoinColumn ánh xạ tới khóa ngoại "order_id", không được null.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;                    // Đơn hàng cha chứa chi tiết này, khóa ngoại tham chiếu đến bảng orders

    // Quan hệ ManyToOne với Product: nhiều chi tiết đơn hàng có thể tham chiếu đến cùng một sản phẩm.
    // FetchType.LAZY giúp trì hoãn load sản phẩm cho đến khi thực sự cần.
    // @JoinColumn ánh xạ tới khóa ngoại "product_id", không được null.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;                // Sản phẩm được mua trong chi tiết này, khóa ngoại tham chiếu đến bảng products

    @Column(nullable = false)
    private Integer quantity;               // Số lượng sản phẩm được mua, không được null

    @Column(nullable = false)
    private Double price;                   // Giá của sản phẩm tại thời điểm đặt hàng, lưu riêng để giữ đúng giá gốc khi giá sản phẩm thay đổi sau này

    public OrderDetail() {}

    public OrderDetail(Order order, Product product, Integer quantity, Double price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
