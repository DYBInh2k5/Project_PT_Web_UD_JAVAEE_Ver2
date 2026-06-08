package com.bookstore.entity;

import jakarta.persistence.*;

// Chi tiết từng sản phẩm trong đơn hàng
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;                    // Đơn hàng cha

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;                // Sản phẩm được mua

    @Column(nullable = false)
    private Integer quantity;               // Số lượng mua

    @Column(nullable = false)
    private Double price;                   // Giá tại thời điểm mua

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
