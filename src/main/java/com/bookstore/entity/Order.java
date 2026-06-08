package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Đơn hàng sau khi khách thanh toán
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                      // Khách hàng đặt

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;        // Ngày đặt hàng

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;             // Tổng tiền

    @Column(nullable = false, columnDefinition = "NVARCHAR(20)")
    private String status;                  // Trạng thái: NEW / SHIPPED / PAID

    @Column(name = "recipient_name", columnDefinition = "NVARCHAR(100)")
    private String recipientName;           // Tên người nhận

    @Column(length = 20)
    private String phone;                   // SĐT người nhận

    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;                 // Địa chỉ giao hàng

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();  // Chi tiết đơn hàng

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) orderDate = LocalDateTime.now();
        if (status == null) status = "NEW";
    }

    public Order() {}

    public Order(User user, Double totalAmount, String recipientName, String phone, String address) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }
}
